package guru.nidi.minecraft.mineedit

/**
 *
 */
case class LatLng(lat: Double, lng: Double) {
  def +(p: LatLng): LatLng = LatLng(lat + p.lat, lng + p.lng)

  def -(p: LatLng): LatLng = LatLng(lat - p.lat, lng - p.lng)
}