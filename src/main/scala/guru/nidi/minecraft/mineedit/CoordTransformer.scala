package guru.nidi.minecraft.mineedit

/**
 *
 */
trait CoordTransformer {
  def mine2model(x: Double, z: Double): (Double, Double)

  def model2mine(elevation: Int): Double
}
