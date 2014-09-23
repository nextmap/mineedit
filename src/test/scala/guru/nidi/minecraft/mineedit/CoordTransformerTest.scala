package guru.nidi.minecraft.mineedit

import java.io.File

import org.scalatest.FlatSpec

/**
 *
 */
class CoordTransformerTest extends FlatSpec {
  val world = WorldReader.load(new File("src/test/resources"))

  behavior of "ElevationEditor"

  it should "work" in {
    val a = System.currentTimeMillis
//    val p0 = LatLng(46.1, 6.9)
//    val p1 = LatLng(46.4, 7.2)
    val p0 = LatLng(34,-10)
//    val p1 = LatLng(81,29)
    val p1 = LatLng(81,20)
    val f = 10
    val ef = Math.cos(p0.lat * Math.PI / 180) * 40000000d / 360
    val size = p1 - p0
    val xl = Math.round(size.lng * f).toInt

    val ct = new LinearCoordTransformer(Xz(0, 0), xl, p0, p1)
    ct.apply(world,
      new AsterElevationModel(new File("/Volumes/MY-HD-1/aster")),
      new ElevationDataApplyer(new LinearIntTransformer(5, f / ef)))
    ct.apply(world,
      new GshhgWaterModel(new File("/Users/nidi/Downloads/gshhg-shp-2.3.2"), p0, p1, xl, ct.mineZlen),
      new WaterDataApplyer)

    WorldWriter.save(new File("target/testout"), world)
    val b = System.currentTimeMillis
    println((b - a) / 1000)
  }

}