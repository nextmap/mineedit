package guru.nidi.minecraft.core

import java.io.File

import guru.nidi.minecraft.core.Block._
import org.scalatest.FlatSpec

/**
 *
 */
class WorldReaderTest extends FlatSpec {
  val world = WorldReader.load(new File("src/test/resources"))
//  val world = WorldReader.load(new File("target/testout"))

  behavior of "Loader"

  it should "load the example regions" in {
  }

  it should "support getBlock for stones" in {
    assert(world.getBlock(10, 4, 10) === STONE)
    assert(world.getBlock(11, 4, 10) === GRANITE)
    assert(world.getBlock(12, 4, 10) === DIORITE)
    assert(world.getBlock(13, 4, 10) === ANDESITE)

    assert(world.getBlock(-1, 4, 10) === STONE)
    assert(world.getBlock(-3, 4, 10) === STONE)

    assert(world.getBlock(-1, 4, -4) === GRANITE)
    assert(world.getBlock(-3, 4, -4) === GRANITE)

    assert(world.getBlock(1, 4, -4) === STONE)
    assert(world.getBlock(3, 4, -4) === STONE)
  }

  it should "support getBlock for water" in {
    assert(world.getBlock(-10, 4, -4) === WATER)
    assert(world.getBlock(-11, 4, -4) === WATER)
    assert(world.getBlock(-12, 4, -4) === WATER_LEVEL7)
    assert(world.getBlock(-10, 5, -6) === FALLING_WATER)
  }

  it should "support getBlock for lava" in {
    assert(world.getBlock(-10, 4, -2) === LAVA)
    assert(world.getBlock(-11, 4, -2) === LAVA)
    assert(world.getBlock(-12, 4, -2) === LAVA_LEVEL6)
    assert(world.getBlock(-13, 4, -2) === LAVA_LEVEL4)
    assert(world.getBlock(-14, 4, -2) === LAVA_LEVEL2)
    assert(world.getBlock(-15, 4, -2) === AIR)
  }
}