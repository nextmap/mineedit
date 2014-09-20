package guru.nidi.minecraft.mineedit

/**
 *
 */
trait CoordTransformer {
  def mine2model(x: Double, z: Double): (Double, Double)
}

class InterpolatingCoordTransformer(x0: Double, y0: Double, x1: Double, y1: Double) extends CoordTransformer {
  def this(t0: (Double, Double), t1: (Double, Double)) = this(t0._1, t0._2, t1._1, t1._2)

  override def mine2model(x: Double, z: Double): (Double, Double) = (x0 + (x1 - x0) * x, y0 + (y1 - y0) * z)
}