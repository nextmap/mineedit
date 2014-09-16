package guru.nidi.geotiff

/**
 *
 */
class GeoTiff(reader: GeoTiffReader,
              val width: Int, val height: Int,
              val bitsPerSample: Int, val info: collection.Map[String, String],
              val xResolution: Double, val yResolution: Double, val resolutionUnit: Int,
              val modelPixelScale: Array[Double],
              val modelTiepoints: Array[Double],
              val geoKeyDirectory: Array[Int]) {

  def getPixel(x: Int, y: Int): Int = reader.getPixel(x, y)
}
