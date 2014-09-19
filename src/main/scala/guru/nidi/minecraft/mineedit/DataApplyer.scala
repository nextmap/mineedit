package guru.nidi.minecraft.mineedit

/**
 *
 */
trait DataApplyer[T] {
  def applyData(world: World, x: Int, z: Int, data: T): Unit
}
