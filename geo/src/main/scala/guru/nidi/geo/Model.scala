package guru.nidi.geo

/**
 *
 */
trait Model[T] {
  def getData(p0: LatLng, p1: LatLng): T
}
