package guru.nidi.minecraft.mineedit

import java.io.File

import org.scalatest.FlatSpec

/**
 *
 */
class ElevationEditorTest extends FlatSpec {
  val world = WorldReader.load(new File("src/test/resources"))

  behavior of "ElevationEditor"

  it should "work" in {
    val ee = new ElevationEditor(
      new InterpolatingCoordTransformer(LatLng(37, 5), LatLng(58, 19), 5, 200),
      new AsterElevationProvider(new File("/Users/nidi/Downloads")),
      new SimpleElevationApplyer)
    ee.setElevation(world, 0, 0, 210, 330)
    WorldWriter.save(new File("target/testout"), world)
  }

}