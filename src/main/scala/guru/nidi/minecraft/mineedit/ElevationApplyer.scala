package guru.nidi.minecraft.mineedit

/**
 *
 */
trait ElevationApplyer {
  def applyElevation(world: World, x: Int, y: Double, z: Int): Unit
}
