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
              val geoKeyDirectory: Array[Int]) extends PixelSource {

  override def getPixel(x: Int, y: Int): Short = reader.getPixel(x, y)

  override def doWithPixels(y: Int, work: (Int, Short) => Unit): Unit = reader.doWithPixels(y, work)
}