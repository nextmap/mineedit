package guru.nidi.minecraft.mineedit

import java.io.File

import org.geotools.data.simple.SimpleFeatureCollection
import org.geotools.data.{FileDataStore, FileDataStoreFinder}
import org.geotools.factory.CommonFactoryFinder
import org.geotools.geometry.jts.{JTS, ReferencedEnvelope}
import org.opengis.feature.simple.SimpleFeature
import org.opengis.filter.Filter

import scala.collection.mutable

/**
 *
 */
class GshhgWaterModel(basedir: File, x0: Double, y0: Double, x1: Double, y1: Double) extends Model[Boolean] {
  def this(basedir: File, p0: (Double, Double), p1: (Double, Double)) = this(basedir, p0._1, p0._2, p1._1, p1._2)

  val lakeStore = store("GSHHS_shp/f/GSHHS_f_L2.shp")
  val schema = lakeStore.getFeatureSource.getSchema
  val targetCRS = schema.getGeometryDescriptor.getCoordinateReferenceSystem
  val ff = CommonFactoryFinder.getFilterFactory2
  val geometryPropertyName = schema.getGeometryDescriptor.getLocalName
  val lakeFeatures = boundedFeatures(lakeStore)
  val seaFeatures = boundedFeatures(store("GSHHS_shp/i/GSHHS_i_L1.shp"))

  def store(file: String) = {
    FileDataStoreFinder.getDataStore(new File(basedir, file))
  }

  def boundedFeatures(store: FileDataStore) = {
    new SimpleFeatureList(store.getFeatureSource.getFeatures(boundingBoxFilter(x0, y0, x1, y1)))
  }

  def boundingBoxFilter(x0: Double, y0: Double, x1: Double, y1: Double) = {
    val box = new ReferencedEnvelope(x0, x1, y0, y1, targetCRS)
    ff.bbox(ff.property(geometryPropertyName), box)
  }

  def intersectsFilter(x0: Double, y0: Double, x1: Double, y1: Double) = {
    val box = new ReferencedEnvelope(x0, x1, y0, y1, targetCRS)
    ff.intersects(ff.property(geometryPropertyName), ff.literal(JTS.toGeometry(box)))
  }

  override def getData(x: Double, y: Double, xl: Double, yl: Double): Boolean = {
    val intersects = intersectsFilter(x, y, x + xl, y + yl)
    seaFeatures.forall(intersects) || lakeFeatures.exists(intersects)
  }
}

class SimpleFeatureList(collection: SimpleFeatureCollection) {
  val list = mutable.Buffer[SimpleFeature]()

  private val iter = collection.features
  while (iter.hasNext) {
    list += iter.next()
  }
  iter.close()

  def exists(filter: Filter): Boolean = {
    list.exists(filter.evaluate(_))
  }

  def forall(filter: Filter): Boolean = {
    list.forall(!filter.evaluate(_))
  }
}