package guru.nidi.geo.aster

import java.io.{File, InputStream}
import java.util.zip.ZipFile

import guru.nidi.geo.tiff.GeoTiffReader
import guru.nidi.geo.{PixelSource, PixelSourceProvider}

/**
 *
 */
class AsterZipPixelSourceProvider(basedir: File) extends PixelSourceProvider {

  import scala.collection.JavaConversions.enumerationAsScalaIterator

  override def sourceFor(lat: Int, lng: Int): Option[PixelSource] = {
    val lngStr = if (lng < 0) "W" + "%03d".format(-lng) else "E" + "%03d".format(lng)
    val latStr = if (lat < 0) "S" + "%02d".format(-lat) else "N" + "%02d".format(lat)
    val name = "ASTGTM2_" + latStr + lngStr
    val zipFile = new File(basedir, latStr + "/" + name + ".zip")

    if (!zipFile.exists()) None
    else {
      val zip = unzip(zipFile)
      Some(GeoTiffReader.read(zip._1, zip._2))
    }
  }

  def unzip(zip: File): (InputStream, Int) = {
    val a = System.currentTimeMillis
    val zipFile = new ZipFile(zip)
    val res = zipFile.entries
      .find(e => e.getName.endsWith("_dem.tif"))
      .map(e => (zipFile.getInputStream(e), e.getSize.toInt))
      .get
    val b = System.currentTimeMillis
    println("unzip " + (b - a))
    res
  }
}
