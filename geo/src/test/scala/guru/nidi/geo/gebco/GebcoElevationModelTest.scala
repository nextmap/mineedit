package guru.nidi.geo.gebco

import java.io.File

import org.scalatest.FlatSpec

/**
 *
 */
class GebcoElevationModelTest extends FlatSpec {

  behavior of "ElevationEditor"

  it should "work" in {
    val gem = new GebcoElevationModel(new File("/Volumes/MY-HD-1/gebco/gridone.nc"))
  }

}