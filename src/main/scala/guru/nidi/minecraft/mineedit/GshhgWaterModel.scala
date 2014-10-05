package guru.nidi.minecraft.mineedit

import java.awt.geom.AffineTransform
import java.awt.image.BufferedImage
import java.awt.{Color, Rectangle}
import java.io.File

import org.geotools.data.{FileDataStore, FileDataStoreFinder}
import org.geotools.geometry.jts.ReferencedEnvelope
import org.geotools.map.{FeatureLayer, MapContent}
import org.geotools.renderer.lite.StreamingRenderer
import org.geotools.styling.{SLD, Style}

/**
 *
 */
//TODO apply projection correctly
class GshhgWaterModel(basedir: File, r0: LatLng, r1: LatLng, xl: Int, yl: Int) extends Model[Boolean] {
  val size = r1 - r0

  private val image = {
    def store(file: String) = FileDataStoreFinder.getDataStore(new File(basedir, file))

    val lakeStore = store("GSHHS_shp/f/GSHHS_f_L2.shp")
    val seaStore = store("GSHHS_shp/h/GSHHS_h_L1.shp")
    val river1Store = store("WDBII_shp/f/WDBII_river_f_L01.shp")
    val river2Store = store("WDBII_shp/f/WDBII_river_f_L02.shp")
    val river3Store = store("WDBII_shp/f/WDBII_river_f_L03.shp")
    val river4Store = store("WDBII_shp/f/WDBII_river_f_L04.shp")
    val schema = lakeStore.getFeatureSource.getSchema
    val targetCRS = schema.getGeometryDescriptor.getCoordinateReferenceSystem

    def featureLayer(store: FileDataStore, style: Style) = new FeatureLayer(store.getFeatureSource, style)

    def addLayer(map: MapContent, store: FileDataStore, style: Style) = map.addLayer(featureLayer(store, style))

    val renderer = new StreamingRenderer()
    val map = new MapContent()
    renderer.setMapContent(map)
    addLayer(map, seaStore, SLD.createPolygonStyle(Color.WHITE, Color.GRAY, 1))
    addLayer(map, lakeStore, SLD.createPolygonStyle(Color.WHITE, Color.BLACK, 1))
    //    addLayer(map, river1Store, SLD.createLineStyle(Color.WHITE, 1f))
    //    addLayer(map, river2Store, SLD.createLineStyle(Color.WHITE, 1f))
    //    addLayer(map, river3Store, SLD.createLineStyle(Color.WHITE, 1f))
    //    addLayer(map, river4Store, SLD.createLineStyle(Color.WHITE, 1f))
    val image = new BufferedImage(xl, yl, BufferedImage.TYPE_BYTE_GRAY)
    val g = image.createGraphics
    g.setColor(Color.GREEN)
    g.drawLine(40, 0, 40, 100)
    def draw(screenX: Int, screenXl: Int, lng0: Double, lng1: Double) = {
      val sx = screenXl / (lng1 - lng0)
      val sy = yl / size.lat
      renderer.paint(g, new Rectangle(screenX, 0, screenXl, yl),
        new ReferencedEnvelope(lng0, lng1, r0.lat, r1.lat, targetCRS),
        new AffineTransform(sx, 0, 0, sy, -lng0 * sx + screenX, -r0.lat * sy))
    }

    val n0 = r0.normalize
    val n1 = r1.normalize
    if (n0.lng < n1.lng) draw(0, xl, n0.lng, n1.lng)
    else {
      val xb = (xl * (180 - n0.lng) / size.lng).toInt
      draw(0, xb, n0.lng, 180)
      draw(xb, xl - xb, -180, n1.lng)
    }
    map.dispose()
    image
  }
//  val frame = new JFrame("ww")
//  val l = new JLabel()
//  l.setIcon(new ImageIcon(image))
//  frame.getContentPane.setLayout(new BorderLayout())
//  frame.getContentPane.add(l, BorderLayout.WEST)
//  frame.setSize(500, 500)
//  frame.setVisible(true)

  //      JMapFrame.showMap(map)
//  Thread.sleep(15000)

  override def getData(p0: LatLng, p1: LatLng): Boolean = {
    val pos = (p0 - r0).normalize
    val mx = Math.round(pos.lng / size.lng * this.xl).toInt
    val my = Math.round(pos.lat / size.lat * this.yl).toInt
    val b = image.getRGB(mx, my)
    (b & 0xff) == 0 || (b & 0xff) == 0xff
  }
}

