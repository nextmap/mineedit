package guru.nidi.minecraft.mineedit

/**
 *
 */
trait Model[T] {
  def getData(p0: LatLng, p1: LatLng): T
}
