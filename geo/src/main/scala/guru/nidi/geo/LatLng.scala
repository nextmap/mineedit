package guru.nidi.geo

/**
 *
 */
case class LatLng(lat: Double, lng: Double) {
  def +(p: LatLng): LatLng = LatLng(lat + p.lat, lng + p.lng)

  def -(p: LatLng): LatLng = LatLng(lat - p.lat, lng - p.lng)

  def normalize: LatLng = {
    LatLng(lat, (lng + 180 + 360000) % 360 - 180) //ensure modulo is always taken from positive value
  }
}