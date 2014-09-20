package guru.nidi.minecraft.mineedit

import java.io.{File, RandomAccessFile}

import guru.nidi.geotiff.{GeoTiff, GeoTiffReader}

/**
 *
 */
class AsterFile(file: File) {
  val raf = new RandomAccessFile(file, "rw")
  val slots = new Array[Byte](360 * 180 * 8)
  if (file.length() > 0) {
    raf.read(slots)
  } else {
    raf.write(slots)
  }

  def importTiffs(dir: File) = {
    dir.listFiles().foreach(f => if (f.getName.startsWith("ASTGTM2_") && f.isDirectory) {
      val slot = slotOf(f.getName)
      val oldPos = getPos(slot)
      val newPos = if (oldPos == 0) raf.length() else oldPos
      val tif = new File(f, f.getName + "_dem.tif")
      setData(newPos, GeoTiffReader.read(tif))
      setPos(slot, newPos)
      tif.delete()
      f.delete()
    })
  }

  def slotOf(name: String): Int = {
    val rawLng = Integer.parseInt(name.substring(12, 15))
    val lng = if (name.charAt(11) == 'E') rawLng else -rawLng
    val rawLat = Integer.parseInt(name.substring(9, 11))
    val lat = if (name.charAt(8) == 'N') rawLat else -rawLat
    slotOf(lat, lng)
  }

  def slotOf(lat: Int, lng: Int): Int = ((180 + lng) * 180 + 90 + lat) * 8

  def getPos(p: Int): Long = {
    (getInt(p).toLong << 32) + (getInt(p + 4) & 0xFFFFFFFFL)
  }

  def getInt(p: Int): Int = {
    (slots(p) << 24) + ((slots(p + 1) & 0xff) << 16) + ((slots(p + 2) & 0xff) << 8) + ((slots(p + 3) & 0xff) << 0)
  }

  def setPos(p: Int, value: Long) = {
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
  }

  def setData(p: Long, value: GeoTiff) = {
    val data = new Array[Byte](2 * 3601 * 3601)
    for (x <- 0 until 3601) {
      for (y <- 0 until 3601) {
        val pixel = value.getPixel(x, y)
        data(dataPos(x, y)) = (pixel >> 8).toByte
        data(dataPos(x, y) + 1) = pixel.toByte
      }
    }
    raf.seek(p)
    raf.write(data)
  }

  def dataPos(x: Int, y: Int) = (x + 3601 * y) * 2

  def getPixel(lat: Int, lng: Int, x: Int, y: Int): Short = {
    val pos = getPos(slotOf(lat, lng))
    if (pos == 0) 0
    else {
      raf.seek(pos + dataPos(x, y))
      raf.readShort()
    }
  }


  def close() = raf.close()
}
