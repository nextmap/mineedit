package guru.nidi.minecraft.mineedit

/**
 *
 */
class ElevationDataApplyer(transformer: DataTransformer[Int, Int]) extends DataApplyer[Int] {
  override def applyData(world: World, x: Int, z: Int, data: Int) = {
    if (data != 0) {
      val level = transformer.model2mine(data)
      val block = if (data > 3500) Block.ICE else if (data > 2000) Block.STONE else Block.GRASS
      for (h <- 0 to level) {
        world.setBlock(x, h, z, block)
      }
    }
  }
}