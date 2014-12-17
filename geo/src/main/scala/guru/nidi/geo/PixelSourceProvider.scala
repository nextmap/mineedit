package guru.nidi.geo


/**
 *
 */
trait PixelSourceProvider {
  def sourceFor(lat: Int, lng: Int): Option[PixelSource]
}




