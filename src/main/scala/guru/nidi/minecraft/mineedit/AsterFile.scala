package guru.nidi.minecraft.mineedit

import java.io.{File, RandomAccessFile}
import java.nio.file.Files
import java.util.zip.ZipFile

import guru.nidi.geotiff.{GeoTiff, GeoTiffReader}

import scala.collection.JavaConversions.enumerationAsScalaIterator


/**
 *
 */
class AsterFile(basedir: File) {

  val startTime = System.currentTimeMillis
  val file = new File(basedir, "aster.ast")
  val raf = new RandomAccessFile(file, "rw")

  object Slots {
    private val slots = new Array[Byte](360 * 180 * 8)
    if (file.length() > 0) {
      raf.read(slots)
    } else {
      raf.write(slots)
    }

    private def slotOf(name: String): Int = {
      val rawLng = Integer.parseInt(name.substring(12, 15))
      val lng = if (name.charAt(11) == 'E') rawLng else -rawLng
      val rawLat = Integer.parseInt(name.substring(9, 11))
      val lat = if (name.charAt(8) == 'N') rawLat else -rawLat
      slotOf(lat, lng)
    }

    private def slotOf(lat: Int, lng: Int): Int = ((180 + lng) * 180 + 90 + lat) * 8

    def getSlot(name: String): Slot = getSlot(slotOf(name))

    def getSlot(lat: Int, lng: Int): Slot = getSlot(slotOf(lat, lng))

    private def getSlot(p: Int): Slot = {
      def getInt(p: Int): Int = {
        (slots(p) << 24) + ((slots(p + 1) & 0xff) << 16) + ((slots(p + 2) & 0xff) << 8) + ((slots(p + 3) & 0xff) << 0)
      }
      Slot((getInt(p).toLong << 32) + (getInt(p + 4) & 0xFFFFFFFFL))
    }

    def setSlot(name: String, slot: Slot): slot.type = setSlot(slotOf(name), slot)

    private def setSlot(p: Int, slot: Slot): slot.type = {
      val value = slot.rawValue
      slots(p + 0) = (value >>> 56).toByte
      slots(p + 1) = (value >>> 48).toByte
      slots(p + 2) = (value >>> 40).toByte
      slots(p + 3) = (value >>> 32).toByte
      slots(p + 4) = (value >>> 24).toByte
      slots(p + 5) = (value >>> 16).toByte
      slots(p + 6) = (value >>> 8).toByte
      slots(p + 7) = (value >>> 0).toByte
      raf.seek(p)
      raf.write(slots, p, 8)
      slot
    }
  }

  import Slots._

  abstract class Slot(val rawValue: Long) {
    def value: Long

    def posOrElse(elsePos: => Long): PosSlot
  }

  object Slot {
    def apply(v: Long) = if ((v & 0x8000000000000000L) == 0) new TimestampSlot(v) else new PosSlot(v & 0x7fffffffffffffffL)
  }

  class PosSlot(val value: Long) extends Slot(value | 0x8000000000000000L) {
    def posOrElse(elsePos: => Long): PosSlot = this
  }

  class TimestampSlot(val value: Long) extends Slot(value) {
    def posOrElse(elsePos: => Long): PosSlot = new PosSlot(elsePos)
  }

  def importTiffs(dir: File) = {
    dir.listFiles().foreach(f => if (f.getName.startsWith("ASTGTM2_") && f.isDirectory) {
      importTiff(f)
    })
  }

  def importTiff(f: File): PosSlot = {
    val newSlot = getSlot(f.getName).posOrElse(raf.length())
    setData(newSlot, GeoTiffReader.read(f))
    f.delete()
    f.getParentFile.delete()
    setSlot(f.getName, newSlot)
  }

  def importTile(lat: Int, lng: Int): Slot = {
    val lngStr = if (lng < 0) "W" + "%03d".format(-lng) else "E" + "%03d".format(lng)
    val latStr = "N"
    val name = "ASTGTM2_" + latStr + "%02d".format(lat) + lngStr
    val dir = new File(basedir, name)
    val zip = new File(basedir, name + ".zip")
    val file = new File(dir, name + "_dem.tif")
    val qa = new File(dir, name + "_num.tif")
    if (!dir.exists() && zip.exists()) unzip(zip, zip.getParentFile)
    if (qa.exists()) qa.delete()
    if (file.exists()) importTiff(file)
    else setSlot(file.getName, new TimestampSlot(System.currentTimeMillis))
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

  def setData(p: PosSlot, value: GeoTiff) = {
    val data = new Array[Byte](2 * 3601 * 3601)
    for (x <- 0 until 3601) {
      for (y <- 0 until 3601) {
        val pixel = value.getPixel(x, y)
        data(dataPos(x, y)) = (pixel >> 8).toByte
        data(dataPos(x, y) + 1) = pixel.toByte
      }
    }
    raf.seek(p.value)
    raf.write(data)
  }

  def dataPos(x: Int, y: Int) = (x + 3601 * y) * 2

  def getPixel(lat: Int, lng: Int, x: Int, y: Int): Short = {
    def eval(slot: Slot): Short = slot match {
      case s: PosSlot =>
        raf.seek(s.value + dataPos(x, y))
        raf.readShort()
      case s: TimestampSlot =>
        if (s.value < startTime) eval(importTile(lat, lng)) else 0
    }

    eval(getSlot(lat, lng))
  }

  def close() = raf.close()
}