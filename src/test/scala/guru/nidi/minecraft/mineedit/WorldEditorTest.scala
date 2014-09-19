package guru.nidi.minecraft.mineedit

import java.io.File

import org.scalatest.FlatSpec

/**
 *
 */
class WorldEditorTest extends FlatSpec {
  val world = WorldReader.load(new File("src/test/resources"))

  behavior of "ElevationEditor"

  it should "work" in {
    val p0 = LatLng(40, 6)
    val p1 = LatLng(60, 10)
    //      new InterpolatingCoordTransformer(LatLng(35, -11), LatLng(63, 19), 5, 200),

    WorldEditor.setElevation(world, 0, 0, 80, 400,
      new WorldEditor(
        new InterpolatingCoordTransformer(p0, p1),
        new AsterElevationModel(new File("/Users/nidi/Downloads")),
        new LinearIntTransformer(5, .005),
        new ElevationDataApplyer),
      new WorldEditor(
        new InterpolatingCoordTransformer(p0, p1),
        new GshhgWaterModel(new File("/Users/nidi/Downloads/gshhg-shp-2.3.2"), p0, p1),
        new NoopTransformer[Boolean],
        new WaterDataApplyer))

    WorldWriter.save(new File("target/testout"), world)
  }

}