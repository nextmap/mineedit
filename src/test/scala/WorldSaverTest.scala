import java.io.File

import guru.nidi.minecraft.mineedit.{WorldLoader, WorldSaver}
import org.scalatest.FlatSpec

/**
 *
 */
class WorldSaverTest extends FlatSpec {
  val world = WorldLoader.load(new File("src/test/resources"))

  behavior of "Saver"

  it should "save the example regions" in {
    val dir = new File("target/testout")
    WorldSaver.save(dir, world)
    val saved = WorldLoader.load(dir)
    assert(saved === world)
    println("W")
  }
}