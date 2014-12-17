package guru.nidi.minecraft.generate

import java.io.File

import guru.nidi.geo.LatLng
import guru.nidi.geo.aster.AsterElevationModel
import guru.nidi.geo.gebco.GebcoElevationModel
import guru.nidi.geo.gshhg.GshhgWaterModel
import guru.nidi.minecraft.core.{WorldReader, WorldWriter}
import org.scalatest.FlatSpec

/**
 *
 */
class CoordTransformerTest extends FlatSpec {
  val world = WorldReader.load(new File("minecraft-generate/src/test/resources"))

  behavior of "ElevationEditor"

  it should "work" in {
    val a = System.currentTimeMillis
    //    val p0 = LatLng(46.1, 6.9)
    //    val p1 = LatLng(46.4, 7.2)
    val p0 = LatLng(46.5, 7.5)
    val p1 = LatLng(47, 8)
    val f = 1000
    val ef = 0.6 * 40000000d / 360
    val size = p1 - p0
    val xl = Math.round(size.lng * f).toInt

    val elevationTrans = new LinearIntTransformer(50, f / ef)
    val ct = new LinearCoordTransformer(Xz(0, 0), xl, p0, p1)

    ct.apply(world,
      new AsterElevationModel(new File("/Volumes/MY-HD-1/aster"), 3600, 0), //System.currentTimeMillis),
      new ElevationDataApplyer(elevationTrans))
    ct.apply(world,
      new GebcoElevationModel(new File("/Volumes/MY-HD-1/gebco/gebco_08.nc")),
      new WaterDepthApplyer(elevationTrans))
    ct.apply(world,
      new GshhgWaterModel(new File("/Volumes/MY-HD-1/gshhg/gshhg-shp-2.3.2"), p0, p1, xl, ct.mineZlen),
      new ShallowWaterApplyer)


    WorldWriter.save(new File("target/testout"), world)
    val b = System.currentTimeMillis
    println((b - a) / 1000)
  }

}