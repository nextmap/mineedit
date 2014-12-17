package guru.nidi.geo.aster

import guru.nidi.geo.{PixelSource, PixelSourceProvider}

/**
 *
 */
class AsterPixelSourceProvider(aster: AsterFile, scale: Int) extends PixelSourceProvider {
   override def sourceFor(lat: Int, lng: Int): Option[PixelSource] = {
     aster.getTile(lat, lng).map(t => new AsterFilePixelSource(t, scale))
   }
 }
