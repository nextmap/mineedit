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
    val a = System.currentTimeMillis
    val p0 = LatLng(46,6)
    val p1 = LatLng(48,8)
    val f = 2000
    val xl = ((p1._1 - p0._1) * f).toInt
    val yl = ((p1._2 - p0._2) * f).toInt

    WorldEditor.setElevation(world, 0, 0, xl, yl,
      new WorldEditor(
        new InterpolatingCoordTransformer(p0, p1),
        new AsterElevationModel(new File("/Users/nidi/Downloads")),
        new LinearIntTransformer(5, .01),
        new ElevationDataApplyer),
      new WorldEditor(
        new InterpolatingCoordTransformer(p0, p1),
        new GshhgWaterModel(new File("/Users/nidi/Downloads/gshhg-shp-2.3.2"), p0, p1, xl, yl),
        new NoopTransformer[Boolean],
        new WaterDataApplyer))

    WorldWriter.save(new File("target/testout"), world)
    val b = System.currentTimeMillis
    println((b - a) / 1000)
  }

}