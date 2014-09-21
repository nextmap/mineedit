package guru.nidi.minecraft.mineedit

/**
 *
 */
trait CoordTransformer {
  def mine2model(x: Double, z: Double): LatLng
}

class InterpolatingCoordTransformer(p0: LatLng, p1: LatLng) extends CoordTransformer {
  override def mine2model(x: Double, z: Double): LatLng =
    LatLng(p0.lat + (p1.lat - p0.lat) * z, p0.lng + (p1.lng - p0.lng) * x)
}