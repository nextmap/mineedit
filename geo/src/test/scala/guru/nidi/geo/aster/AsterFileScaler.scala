package guru.nidi.geo.aster

import java.io.File

/**
 *
 */
object AsterFileScaler {
  def main(args: Array[String]) {
    val basedir = new File("/Volumes/MY-HD-1/aster")
    val source = new AsterFile(basedir, 3600, new AsterZipPixelSourceProvider(basedir), true, System.currentTimeMillis)
    val target = new AsterFile(basedir, 1800, new AsterPixelSourceProvider(source, 2), false, System.currentTimeMillis)
    for (lat <- -82 until 82) {
      for (lng <- -70 until -60) {
        println(lat + " " + lng)
        target.getPixel(lat, lng, 0, 0)
      }
    }
  }
}
