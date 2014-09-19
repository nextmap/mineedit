package guru.nidi.minecraft.mineedit

import java.io.File

import org.geotools.data.FileDataStoreFinder
import org.geotools.factory.CommonFactoryFinder
import org.geotools.geometry.jts.{JTS, ReferencedEnvelope}

/**
 *
 */
class GshhgWaterModel(basedir: File, x0: Double, y0: Double, x1: Double, y1: Double) extends Model[Boolean] {
  def this(basedir: File, p0: (Double, Double), p1: (Double, Double)) = this(basedir, p0._1, p0._2, p1._1, p1._2)

  val lakeStore = FileDataStoreFinder.getDataStore(new File(basedir, "GSHHS_shp/f/GSHHS_f_L2.shp"))
  val schema = lakeStore.getFeatureSource.getSchema
  val ff = CommonFactoryFinder.getFilterFactory2
  val geometryPropertyName = schema.getGeometryDescriptor.getLocalName
  val lakeFeatures = lakeStore.getFeatureSource.getFeatures(boundingBoxFilter(x0, y0, x1, y1))

  def boundingBoxFilter(x0: Double, y0: Double, x1: Double, y1: Double) = {
    val targetCRS = schema.getGeometryDescriptor.getCoordinateReferenceSystem
    val box = new ReferencedEnvelope(x0, x1, y0, y1, targetCRS)
    ff.bbox(ff.property(geometryPropertyName), box)
  }

  def containsFilter(x0: Double, y0: Double, x1: Double, y1: Double) = {
    val targetCRS = schema.getGeometryDescriptor.getCoordinateReferenceSystem
    val box = new ReferencedEnvelope(x0, x1, y0, y1, targetCRS)
    ff.intersects(ff.property(geometryPropertyName), ff.literal(JTS.toGeometry(box)))
  }

  override def getData(x: Double, y: Double, xl: Double, yl: Double): Boolean = {
    lakeFeatures.subCollection(containsFilter(x, y, x + xl, y + yl)).size() > 0
  }
}
