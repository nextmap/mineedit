package guru.nidi.minecraft.mineedit

/**
 *
 */
class ElevationDataApplyer extends DataApplyer[Int] {
  override def applyData(world: World, x: Int, z: Int, data: Int) = {
    for (h <- 0 until data.toInt) {
      world.setBlock(x, h, z, if (h > 18) Block.ICE else if (h > 12) Block.STONE else Block.GRASS)
    }
  }
}
