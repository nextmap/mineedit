import java.io.File

import guru.nidi.minecraft.mineedit.{World, Loader}
import org.scalatest.FlatSpec

/**
 *
 */
class LoaderTest extends FlatSpec {
  behavior of "Loader"

  it should "load the example regions" in {
    val world: World = Loader.load(new File("src/test/resources"))
  }
}
