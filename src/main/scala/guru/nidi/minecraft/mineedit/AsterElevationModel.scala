package guru.nidi.minecraft.mineedit

import java.io.File
import java.nio.file.Files
import java.util.zip.ZipFile

import com.google.common.cache.{CacheBuilder, CacheLoader}
import guru.nidi.geotiff.{GeoTiff, GeoTiffReader}

import scala.collection.JavaConversions.enumerationAsScalaIterator

/**
 *
 */
class AsterElevationModel(basedir: File) extends Model[Int] {
  val models = CacheBuilder.newBuilder()
    .maximumSize(50)
    .build(new CacheLoader[(Int, Int), GeoTiff] {
    def load(key: (Int, Int)): GeoTiff = {
      loadModel(key._1, key._2)
    }
  })


  override def getData(x: Double, y: Double, xl: Double, yl: Double): Int = {
    (getElevation(x, y) + getElevation(x + xl, y) + getElevation(x, y + yl) + getElevation(x + xl, y + yl)) / 4
  }

  def getElevation(x: Double, y: Double): Int = {
    val bx = intPart(x)
    val by = intPart(y)
    val model = models.get((bx, by))
    model.getPixel((3600 * (x - bx)).toInt, (3600 * (1 - y + by)).toInt)
  }

  def intPart(v: Double) = (if (v < 0) v - 1 else v).toInt

  def addModel(x: Int, y: Int, model: GeoTiff): GeoTiff = {
    models.put((x, y), model)
    model
  }

  def loadModel(x: Int, y: Int): GeoTiff = {
    val lng = if (x < 0) "W" + "%03d".format(-x) else "E" + "%03d".format(x)
    val lat = "N"
    val name = "ASTGTM2_" + lat + "%02d".format(y) + lng
    val dir = new File(basedir, name)
    val zip = new File(basedir, name + ".zip")
    val file = new File(dir, name + "_dem.tif")
    val qa = new File(dir, name + "_num.tif")
    if (!dir.exists() && zip.exists()) unzip(zip, zip.getParentFile)
    if (qa.exists()) qa.delete()
    if (file.exists()) GeoTiffReader.read(file)
    else new EmptyGeoTiff()
  }

  def unzip(zip: File, target: File) = {
    val zipFile = new ZipFile(zip)
    for (entry <- zipFile.entries()) {
      val destFile = new File(target, entry.getName)
      destFile.getParentFile.mkdirs
      if (!entry.isDirectory) {
        Files.copy(zipFile.getInputStream(entry), destFile.toPath)
      }
    }
  }
}

class EmptyGeoTiff extends GeoTiff(null, 3601, 3601, 8, null, 100, 100, 2, null, null, null) {
  override def getPixel(x: Int, y: Int): Int = 0
}
