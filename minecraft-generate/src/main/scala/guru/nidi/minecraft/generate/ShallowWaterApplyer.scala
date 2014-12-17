package guru.nidi.minecraft.generate

import guru.nidi.minecraft.core.{Block, World}

/**
 *
 */
class ShallowWaterApplyer extends DataApplyer[Boolean] {
  override def applyData(world: World, x: Int, z: Int, data: Boolean) = {
    if (data) {
      world.setBlock(x, world.maxHeight(x, z), z, Block.WATER)
    }
  }
}
