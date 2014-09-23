package guru.nidi.minecraft.mineedit

import java.io.File

import guru.nidi.geotiff.GeoTiff


/**
 *
 */
class AsterElevationModel(basedir: File) extends Model[Int] {
  val asterFile = new AsterFile(basedir)

  override def getData(p0: LatLng, p1: LatLng): Int = {
    (getElevation(p0.lng, p0.lat) + getElevation(p1.lng, p0.lat) + getElevation(p0.lng, p1.lat) + getElevation(p1.lng, p1.lat)) / 4
  }

  def getElevation(x: Double, y: Double): Int = {
    val bx = intPart(x)
    val by = intPart(y)
    asterFile.getPixel(by, bx, (3600 * (x - bx)).toInt, (3600 * (1 - y + by)).toInt)
  }

  def intPart(v: Double) = (if (v < 0) v - 1 else v).toInt
}

class EmptyGeoTiff extends GeoTiff(null, 3601, 3601, 8, null, 100, 100, 2, null, null, null) {
  override def getPixel(x: Int, y: Int): Int = 0
}
