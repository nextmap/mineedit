package guru.nidi.minecraft.mineedit

/**
 *
 */
class WaterDataApplyer extends DataApplyer[Boolean] {
  override def applyData(world: World, x: Int, z: Int, data: Boolean) = {
    if (data) {
      world.setBlock(x, world.maxHeight(x, z), z, Block.WATER)
    }
  }
}
