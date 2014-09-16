package guru.nidi.geotiff

import java.io.File

import org.scalatest.FlatSpec

/**
 *
 */
class GeoTiffReaderTest extends FlatSpec {
  it should "read an example geo tiff" in {
    val tiff = GeoTiffReader.read(new File("/Users/nidi/Downloads/ASTGTM2_N46E007/ASTGTM2_N46E007_dem.tif"))
    for (x <- 0 until tiff.width) println(tiff.getPixel(x, 0))
  }
}
