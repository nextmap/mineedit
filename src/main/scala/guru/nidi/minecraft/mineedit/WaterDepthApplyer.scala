package guru.nidi.minecraft.mineedit

/**
 *
 */
class WaterDepthApplyer(transformer: DataTransformer[Int, Int]) extends DataApplyer[Int] {
  val zero = transformer.model2mine(0)
  val m = transformer.model2mine(-500)
  if (m < 1) throw new IllegalArgumentException(s"water depth of -500=$m")

  override def applyData(world: World, x: Int, z: Int, data: Int) = {
    if (data < 0 && world.getBlock(x, m, z) == Block.AIR) {
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
