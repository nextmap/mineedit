package guru.nidi.geo.aster

import guru.nidi.geo.PixelSource

/**
 *
 */
class AsterFilePixelSource(tile: Tile, scale: Int) extends PixelSource {
   override def getPixel(x: Int, y: Int): Short =
     tile.getPixel(x * scale, y * scale)


   override def doWithPixels(y: Int, work: (Int, Short) => Unit): Unit = {
     tile.doWithPixels(y, scale, work)
   }
 }
