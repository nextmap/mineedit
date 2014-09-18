package guru.nidi.minecraft.mineedit

import java.io.File

import guru.nidi.geotiff.GeoTiffReader
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

    val tiff = GeoTiffReader.read(new File("/Users/nidi/Downloads/ASTGTM2_N46E008/ASTGTM2_N46E008_dem.tif"))
    for (x <- 0 until 200)
      for (y <- 0 until 200) {
        val m = tiff.getPixel(x * 15, y * 15)
//        println(m)
        for (h <- 4 until m / 80) {
          world.setBlock(x - 50, h, y - 50, if (h > 40) Block.ICE else if (h > 30) Block.STONE else Block.DIRT)
        }
        if (m / 80 > 35) world.setBlock(x - 50, m / 80, y - 50, Block.SNOW)
        for (h <- m / 80 until 10) {
          world.setBlock(x - 50, h, y - 50, Block.WATER)
        }
      }
    WorldWriter.save(dir, world)
    val saved = WorldReader.load(dir)
    assert(saved === world)
  }

}