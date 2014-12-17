package guru.nidi.geo

/**
 *
 */
trait PixelSource {

  def getPixel(x: Int, y: Int): Short

  def doWithPixels(y: Int, work: (Int, Short) => Unit): Unit
}



