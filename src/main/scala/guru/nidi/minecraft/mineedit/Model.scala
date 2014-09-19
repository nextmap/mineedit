package guru.nidi.minecraft.mineedit

/**
 *
 */
trait Model[T] {
  def getData(x: Double, y: Double, xl: Double, yl: Double): T
}
