package guru.nidi.minecraft.mineedit

/**
 *
 */
object Util {
  def floorDiv(x: Int, y: Int): Int = {
    val r = x / y
    if ((x ^ y) < 0 && (r * y != x)) r - 1 else r
  }

  def floorMod(x: Int, y: Int): Int = x - floorDiv(x, y) * y
}
