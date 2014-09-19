package guru.nidi.minecraft.mineedit

/**
 *
 */
class SimpleElevationApplyer extends ElevationApplyer {
  override def applyElevation(world: World, x: Int, y: Double, z: Int) = {
    if (y == 5) {
      for (h <- 0 until y.toInt) {
        world.setBlock(x, h, z, Block.WATER)
      }
    } else {
      for (h <- 0 until y.toInt) {
        world.setBlock(x, h, z, if (h > 18) Block.ICE else if (h > 12) Block.STONE else Block.DIRT)
      }
    }
  }
}
