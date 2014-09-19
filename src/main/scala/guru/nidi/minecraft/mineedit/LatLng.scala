package guru.nidi.minecraft.mineedit

/**
 *
 */
class LatLng(lat: Double, lng: Double) extends Tuple2(lng, lat)

object LatLng {
  def apply(lat: Double, lng: Double) = new LatLng(lat, lng)
}
