import java.io.File

import guru.nidi.minecraft.mineedit.{WorldReader, WorldWriter}
import org.scalatest.FlatSpec

/**
 *
 */
class WorldWriterTest extends FlatSpec {
  val world = WorldReader.load(new File("src/test/resources"))

  behavior of "Saver"

  it should "save the example regions" in {
    val dir = new File("target/testout")
    WorldWriter.save(dir, world)
    val saved = WorldReader.load(dir)
    assert(saved === world)
    println("W")
  }
}