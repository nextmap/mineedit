package guru.nidi.minecraft.mineedit

import java.io.File

import org.scalatest.FlatSpec

/**
 *
 */
class GebcoElevationModelTest extends FlatSpec {

  behavior of "ElevationEditor"

  it should "work" in {
    val gem = new GebcoElevationModel(new File("/Users/nidi/Downloads/gridone.nc"))
  }

}