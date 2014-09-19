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
class AsterElevationProvider(basedir: File) extends ElevationProvider {
  val models = CacheBuilder.newBuilder()
    .maximumSize(50)
    .build(new CacheLoader[(Int, Int), GeoTiff] {
    def load(key: (Int, Int)): GeoTiff = {
      loadModel(key._1, key._2)
    }
  })
  //    .Map[(Int, Int), GeoTiff]()

  override def getElevation(x: Double, y: Double): Int = {
    val bx = x.toInt
    val by = y.toInt
    val model = models.get((bx, by))
    //    val model = models.getOrElse((bx, by), addModel(bx, by, loadModel(bx, by)))
    model.getPixel((3600 * (x - bx)).toInt, (3600 * (1 - y + by)).toInt)
  }

  def addModel(x: Int, y: Int, model: GeoTiff): GeoTiff = {
    models.put((x, y), model)
    model
  }

  def loadModel(x: Int, y: Int): GeoTiff = {
    val name = "ASTGTM2_N%02dE%03d".format(y, x)
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
