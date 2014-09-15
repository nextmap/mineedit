import java.io.File

import guru.nidi.minecraft.mineedit.{Block, Loader}
import org.scalatest.FlatSpec

/**
 *
 */
class LoaderTest extends FlatSpec {
  val world = Loader.load(new File("src/test/resources"))

  behavior of "Loader"

  it should "load the example regions" in {
  }

  it should "support chunk level getters" in {
    assert(world.getBlock(10, 4, 10) === Block.STONE)
    assert(world.getBlock(11, 4, 10) === Block.GRANITE)
    assert(world.getBlock(12, 4, 10) === Block.DIORITE)
    assert(world.getBlock(13, 4, 10) === Block.ANDESITE)

    assert(world.getBlock(-1, 4, 10) === Block.STONE)
    assert(world.getBlock(-3, 4, 10) === Block.STONE)

    assert(world.getBlock(-1, 4, -4) === Block.GRANITE)
    assert(world.getBlock(-3, 4, -4) === Block.GRANITE)

    assert(world.getBlock(1, 4, -4) === Block.STONE)
    assert(world.getBlock(3, 4, -4) === Block.STONE)
  }
}
