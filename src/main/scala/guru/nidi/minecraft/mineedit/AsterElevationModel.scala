package guru.nidi.minecraft.mineedit

import java.io.File

import guru.nidi.geotiff.ZipPixelSourceProvider


/**
 *
 */
class AsterElevationModel(basedir: File, resolution: Int, minTime: Long) extends Model[Int] {
  val asterFile = new AsterFile(basedir, resolution, new ZipPixelSourceProvider(basedir), false, minTime)

  override def getData(p0: LatLng, p1: LatLng): Int = {
    asterFile.getPixel(p0)
    //    (getElevation(p0.lng, p0.lat) + getElevation(p1.lng, p0.lat) + getElevation(p0.lng, p1.lat) + getElevation(p1.lng, p1.lat)) / 4
  }
}
