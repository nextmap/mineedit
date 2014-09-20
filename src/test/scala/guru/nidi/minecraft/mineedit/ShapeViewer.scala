package guru.nidi.minecraft.mineedit

import java.awt.Color
import java.io.File

import org.geotools.data.FileDataStoreFinder
import org.geotools.factory.CommonFactoryFinder
import org.geotools.geometry.jts.ReferencedEnvelope
import org.geotools.map.{FeatureLayer, MapContent}
import org.geotools.styling.SLD
import org.geotools.swing.JMapFrame
import org.opengis.feature.`type`.FeatureType

/**
 *
 */
object ShapeViewer {
  def main(args: Array[String]) {
//    val store = FileDataStoreFinder.getDataStore(new File("/Users/nidi/Downloads/gshhg-shp-2.3.2/GSHHS_shp/f/GSHHS_f_L2.shp"))
          val store = FileDataStoreFinder.getDataStore(new File("/Users/nidi/Downloads/gshhg-shp-2.3.2/WDBII_shp/c/WDBII_river_c_L02.shp"))
    val source = store.getFeatureSource
    val schema = source.getSchema
    val features = source.getFeatures()//boundingBoxFilter(schema, -20, 30, 20, 60))
    val map = new MapContent()
    map.setTitle("Quickstart")
    val style = SLD.createPolygonStyle(Color.BLACK,Color.BLUE,1)
//    val style = SLD.createSimpleStyle(schema)
    val layer = new FeatureLayer(features, style)
    map.addLayer(layer)
    JMapFrame.showMap(map)
  }

  def boundingBoxFilter(schema: FeatureType, x0: Double, y0: Double, x1: Double, y1: Double) = {
    val targetCRS = schema.getGeometryDescriptor.getCoordinateReferenceSystem
    val box = new ReferencedEnvelope(x0, x1, y0, y1, targetCRS)
    val ff = CommonFactoryFinder.getFilterFactory2
    val geometryPropertyName = schema.getGeometryDescriptor.getLocalName
    ff.bbox(ff.property(geometryPropertyName), box)
  }
}
