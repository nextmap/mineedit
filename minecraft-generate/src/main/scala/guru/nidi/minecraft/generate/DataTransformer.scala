package guru.nidi.minecraft.generate

/**
 *
 */
trait DataTransformer[S, T] {
  def model2mine(data: S): T
}

class NoopTransformer[T] extends DataTransformer[T, T] {
  override def model2mine(data: T): T = data
}

class LinearIntTransformer(a: Int, b: Double) extends DataTransformer[Int, Int] {
  override def model2mine(data: Int): Int = a + (b * data).toInt
}
