package guru.nidi.minecraft.core

import java.io.File

import org.scalatest.FlatSpec

/**
 *
 */
class WorldWriterTest extends FlatSpec {
  val world = WorldReader.load(new File("src/test/resources"))

  behavior of "Saver"

  it should "create a new section if needed" in {
    world.setBlock(0, 16, 0, Block.BIRCH_WOOD_UP_DOWN)
  }

  it should "create a new chunk if needed" in {
    world.setBlock(351, 4, 161, Block.BIRCH_WOOD_UP_DOWN)
  }

  it should "create a new region if needed" in {
    world.setBlock(-512, 4, 0, Block.BIRCH_WOOD_UP_DOWN)
  }

  it should "save the example regions" in {
    val dir = new File("target/testout")
    WorldWriter.save(dir, world)
    val saved = WorldReader.load(dir)
    assert(saved === world)
  }

}