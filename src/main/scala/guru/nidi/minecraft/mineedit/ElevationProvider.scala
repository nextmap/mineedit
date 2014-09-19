package guru.nidi.minecraft.mineedit

/**
 *
 */
trait ElevationProvider {
  def getElevation(x: Double, y: Double): Int
}
