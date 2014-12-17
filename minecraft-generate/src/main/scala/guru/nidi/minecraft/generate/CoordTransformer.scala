package guru.nidi.minecraft.generate

import guru.nidi.geo.{LatLng, Model}
import guru.nidi.minecraft.core.World
import org.geotools.geometry.DirectPosition2D
import org.geotools.referencing.CRS
import org.geotools.referencing.crs.AbstractDerivedCRS
import org.geotools.referencing.operation.DefaultProjection

/**
 *
 */
trait CoordTransformer {
  def apply[T](world: World, model: Model[T], applyer: DataApplyer[T])

  def mine2model(x: Int, z: Int): LatLng
}

class LinearCoordTransformer(minePos: Xz, mineXlen: Int, model0: LatLng, model1: LatLng) extends CoordTransformer {
  val modelSize = model1 - model0
  val mineZlen = (mineXlen / modelSize.lng * modelSize.lat).toInt

  override def mine2model(x: Int, z: Int): LatLng = {
    val p = (model0 + LatLng(
      modelSize.lat * (z - minePos.z) / mineZlen,
      modelSize.lng * (x - minePos.x) / mineXlen)).normalize
    val d = new DirectPosition2D()
    t.transform(new DirectPosition2D(p.lng , p.lat), d)
    LatLng(d.y *10000, d.x*10000)
    p
  }

  val eckert = CRS.parseWKT("PROJCS[\"World_Eckert_IV\"," +
    "GEOGCS[\"WGS_1984\",DATUM[\"WGS_1984\",SPHEROID[\"WGS_1984\",6378137.0,298.257223563]]," +
    "PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]]," +
    "PROJECTION[\"Eckert_IV\"]," +
    "PARAMETER[\"Central_Meridian\",0.0],UNIT[\"Meter\",1.0]]")
  val t = eckert.asInstanceOf[AbstractDerivedCRS].getConversionFromBase.asInstanceOf[DefaultProjection].getMathTransform.inverse

  //  val t = new EckertIV(null).inverse()

  def apply[T](world: World, model: Model[T], applyer: DataApplyer[T]) = {
    val blockSize = 5
    val xmax = minePos.x + mineXlen
    val zmax = minePos.z + mineZlen
    for (xb <- minePos.x until xmax - 1 by blockSize) {
      for (zb <- minePos.z until zmax - 1 by blockSize) {
        println(s"($xb,$zb)")

        for (x <- xb until Math.min(xb + blockSize, xmax - 1)) {
          for (z <- zb until Math.min(zb + blockSize, zmax - 1)) {
            val modelCoord0 = mine2model(x, z)
            println(s"($x,$z)->(${modelCoord0.lat},${modelCoord0.lng})")
            val modelCoord1 = mine2model(x + 1, z + 1)
            val data = model.getData(modelCoord0, modelCoord1)
            applyer.applyData(world, x, zmax - z, data)
          }
        }
      }
    }

  }

}