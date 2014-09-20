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
class GshhgWaterModel(basedir: File, x0: Double, y0: Double, x1: Double, y1: Double, xl: Int, yl: Int) extends Model[Boolean] {
  def this(basedir: File, p0: (Double, Double), p1: (Double, Double), xl: Int, yl: Int) = this(basedir, p0._1, p0._2, p1._1, p1._2, xl, yl)


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
    addLayer(map, river1Store, SLD.createLineStyle(Color.WHITE, 2.5f))
    addLayer(map, river2Store, SLD.createLineStyle(Color.WHITE, 2f))
    addLayer(map, river3Store, SLD.createLineStyle(Color.WHITE, 1.5f))
    addLayer(map, river4Store, SLD.createLineStyle(Color.WHITE, 1f))
    val image = new BufferedImage(xl, yl, BufferedImage.TYPE_BYTE_GRAY)
    val g = image.createGraphics
    val sx = xl / (x1 - x0)
    val sy = yl / (y1 - y0)
    renderer.paint(g, new Rectangle(0, 0, xl, yl),
      new ReferencedEnvelope(x0, x1, y0, y1, targetCRS),
      new AffineTransform(sx, 0, 0, sy, -x0 * sx, -y0 * sy))
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

  //    JMapFrame.showMap(map)
  //  Thread.sleep(15000)

  override def getData(x: Double, y: Double, xl: Double, yl: Double): Boolean = {
    val mx = Math.round((x - x0) / (x1 - x0) * this.xl).toInt
    val my = Math.round((y - y0) / (y1 - y0) * this.yl).toInt
    val b = image.getRGB(mx, my)
    (b & 0xff) == 0 || (b & 0xff) == 0xff
  }
}

