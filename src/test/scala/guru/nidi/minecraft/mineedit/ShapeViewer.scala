package guru.nidi.minecraft.mineedit

import java.awt.Color
import java.io.File

import org.geotools.data.FileDataStoreFinder
import org.geotools.factory.CommonFactoryFinder
import org.geotools.geometry.DirectPosition2D
import org.geotools.geometry.jts.ReferencedEnvelope
import org.geotools.map.{FeatureLayer, MapContent}
import org.geotools.referencing.CRS
import org.geotools.referencing.crs.DefaultGeographicCRS
import org.geotools.styling.SLD
import org.geotools.swing.JMapFrame
import org.opengis.feature.`type`.FeatureType

/**
 *
 */
object ShapeViewer {
  def main(args: Array[String]) {
    val store = FileDataStoreFinder.getDataStore(new File("/Users/nidi/Downloads/gshhg-shp-2.3.2/GSHHS_shp/l/GSHHS_l_L1.shp"))
    //          val store = FileDataStoreFinder.getDataStore(new File("/Users/nidi/Downloads/gshhg-shp-2.3.2/WDBII_shp/c/WDBII_river_c_L02.shp"))
    val source = store.getFeatureSource
    val schema = source.getSchema
    val features = source.getFeatures() //boundingBoxFilter(schema, -20, 30, 20, 60))
    val map = new MapContent()
    map.setTitle("Quickstart")
    val style = SLD.createPolygonStyle(Color.BLACK, Color.BLUE, 1)
    //    val style = SLD.createSimpleStyle(schema)
    val layer = new FeatureLayer(features, style)
    map.addLayer(layer)
    val lb = layer.getBounds
    val b = map.getViewport.getBounds
    map.getViewport.setBounds(new ReferencedEnvelope(-180, 180, -60, 60, layer.getBounds.getCoordinateReferenceSystem))
    //    map.getViewport.setScreenArea(new Rectangle(100, 100, 200, 200))
    //    for (auth <- CRS.getSupportedAuthorities(false)) {
    //      for (code <- CRS.getSupportedCodes(auth)) {
    //        try {
    //          val crs = CRS.decode(auth + ":" + code)
    //          val extent = crs.getDomainOfValidity.getGeographicElements.iterator().next().asInstanceOf[GeographicBoundingBox]
    //          if (extent.getNorthBoundLatitude > 70 && extent.getSouthBoundLatitude < -70) {
    //            println(auth + ":" + code + " " + crs.getName + "(" + extent + ")")
    //          }
    //        } catch {
    //          case _ =>
    //        }
    //      }
    //    }
    //    map.getViewport.setCoordinateReferenceSystem(CRS.decode("EPSG:4330"))
    val eckert = CRS.parseWKT("PROJCS[\"World_Eckert_IV\"," +
      "GEOGCS[\"WGS_1984\",DATUM[\"WGS_1984\",SPHEROID[\"WGS_1984\",6378137.0,298.257223563]]," +
      "PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]]," +
      "PROJECTION[\"Eckert_IV\"]," +
      "PARAMETER[\"Central_Meridian\",0.0],UNIT[\"Meter\",1.0]]")
    val wt = CRS.parseWKT("PROJCS[\"World_Eckert_IV\"," +
      "GEOGCS[\"WGS_1984\",DATUM[\"WGS_1984\",SPHEROID[\"WGS_1984\",6378137.0,298.257223563]]," +
      "PRIMEM[\"Greenwich\",0.0],UNIT[\"Degree\",0.0174532925199433]]," +
      "PROJECTION[\"Robinson\"]," +
      "UNIT[\"Meter\",1.0]]")
    val google = CRS.parseWKT("PROJCS[\"WGS 84 / Pseudo-Mercator\"," +
      "GEOGCS[\"WGS 84\",DATUM[\"World Geodetic System 1984\",SPHEROID[\"WGS 84\", 6378137.0, 298.257223563]]," +
      "PRIMEM[\"Greenwich\", 0.0],UNIT[\"degree\", 0.017453292519943295]]," +
      "PROJECTION[\"Popular Visualisation Pseudo Mercator\"]," +
      "PARAMETER[\"false_northing\", 0.0],UNIT[\"m\", 1.0]]")
    map.getViewport.setCoordinateReferenceSystem(eckert)
    //    map.getViewport.getBounds.getCoordinateReferenceSystem.
    val crs = CRS.decode("EPSG:3857")
    println(crs.toWKT)



    val coordinateOperationFactory = CRS.getCoordinateOperationFactory(true)
    val operation = coordinateOperationFactory.createOperation(DefaultGeographicCRS.WGS84, map.getViewport.getCoordinateReferenceSystem)
    val d = new DirectPosition2D()
    operation.getMathTransform.transform(new DirectPosition2D(180,0), d)

    //    val transformed: GeneralEnvelope = CRS.transform(operation, this)


    //    map.getViewport.setCoordinateReferenceSystem()
    val frame = new JMapFrame(map)
    frame.enableStatusBar(true)
    frame.enableToolBar(true)
    frame.initComponents()
    frame.setSize(500, 400)
    frame.setVisible(true)
    JMapFrame.showMap(map)
    print(0)
  }

  def boundingBoxFilter(schema: FeatureType, x0: Double, y0: Double, x1: Double, y1: Double) = {
    val targetCRS = schema.getGeometryDescriptor.getCoordinateReferenceSystem
    val box = new ReferencedEnvelope(x0, x1, y0, y1, targetCRS)
    val ff = CommonFactoryFinder.getFilterFactory2
    val geometryPropertyName = schema.getGeometryDescriptor.getLocalName
    ff.bbox(ff.property(geometryPropertyName), box)
  }
}
