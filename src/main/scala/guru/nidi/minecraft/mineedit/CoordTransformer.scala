package guru.nidi.minecraft.mineedit

/**
 *
 */
trait CoordTransformer {
  def mine2model(x: Double, z: Double): LatLng
}

class InterpolatingCoordTransformer(p0: LatLng, p1: LatLng) extends CoordTransformer {
  val size = p1 - p0

  override def mine2model(x: Double, z: Double): LatLng = {
    p0 + LatLng(size.lat * z, size.lng * x)
  }
}