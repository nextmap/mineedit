package guru.nidi.minecraft.generate

import guru.nidi.minecraft.core.World

/**
 *
 */
trait DataApplyer[T] {
  def applyData(world: World, x: Int, z: Int, data: T): Unit
}
