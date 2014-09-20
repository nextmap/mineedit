package guru.nidi.minecraft.mineedit

/**
 *
 */
class ElevationDataApplyer extends DataApplyer[Int] {
  override def applyData(world: World, x: Int, z: Int, data: Int) = {
    for (h <- 0 until data.toInt) {
      world.setBlock(x, h, z, if (h > 31) Block.ICE else if (h > 19) Block.STONE else Block.GRASS)
    }
  }
}
