package guru.nidi.minecraft.generate

import guru.nidi.minecraft.core.{Block, World}

/**
 *
 */
class WaterDepthApplyer(transformer: DataTransformer[Int, Int]) extends DataApplyer[Int] {
  val zero = transformer.model2mine(0)
  val deep = transformer.model2mine(-1000)
  if (deep < 5) throw new IllegalArgumentException(s"water depth of -500=$deep")

  override def applyData(world: World, x: Int, z: Int, data: Int) = {
    if (world.getBlock(x, deep, z) == Block.AIR) {
      val level = transformer.model2mine(data)
      if (level < 1) throw new IllegalArgumentException(s"water depth of $data=$level")
      for (h <- 0 until level) {
        world.setBlock(x, h, z, Block.GRANITE)
      }
      for (h <- level to zero) {
        world.setBlock(x, h, z, Block.WATER)
      }
    }
  }
}
