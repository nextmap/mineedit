package guru.nidi.geo.gebco

import java.io.File

import guru.nidi.geo.{LatLng, Model}
import ucar.nc2.dataset.NetcdfDataset

/**
 *
 */
class GebcoElevationModel(file: File) extends Model[Int] {
  val dataset = NetcdfDataset.openDataset(file.toURI.toString, false, null)
  val xrange = dataset.findVariable("x_range").read()
  val yrange = dataset.findVariable("y_range").read()
  val dim = dataset.findVariable("dimension").read()
  val z = dataset.findVariable("z").read()

  override def getData(p0: LatLng, p1: LatLng): Int = {
    val x = (p0.lng - xrange.getDouble(0)) / (xrange.getDouble(1) - xrange.getDouble(0))
    val y = 1 - (p0.lat - yrange.getDouble(0)) / (yrange.getDouble(1) - yrange.getDouble(0))
    z.getShort((x * dim.getInt(0)).toInt + (y * dim.getInt(1)).toInt * dim.getInt(0))
  }
}
