package guru.nidi.minecraft.mineedit

/**
 *
 */
class InterpolatingCoordTransformer(x0: Double, y0: Double, x1: Double, y1: Double, e0: Int, ef: Int) extends CoordTransformer {
  def this(t0: (Double, Double), t1: (Double, Double), e0: Int, ef: Int) = this(t0._1, t0._2, t1._1, t1._2, e0, ef)

  override def mine2model(x: Double, z: Double): (Double, Double) = (x0 + (x1 - x0) * x, y1 - .0001 + (y0 - y1) * z)

  override def model2mine(elevation: Int): Double = e0 + elevation / ef.toDouble
}
