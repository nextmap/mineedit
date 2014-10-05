package guru.nidi.minecraft.mineedit

import java.io.{File, RandomAccessFile}
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

import com.google.common.cache._
import guru.nidi.geotiff.{PixelSource, PixelSourceProvider}


/**
 *
 */
class AsterFile(basedir: File, val resolution: Int, sourceProvider: PixelSourceProvider, useCache: Boolean,minTime:Long) {
  val file = new File(basedir, s"aster-$resolution.ast")
  val raf = new RandomAccessFile(file, "rw")
  val slotsSize = 360 * 180 * 8
  val pageSize = 2 * (resolution + 1) * (resolution + 1)

  val cache: LoadingCache[java.lang.Integer, ByteBuffer] = CacheBuilder.newBuilder()
    .maximumSize(300)
    .removalListener(new RemovalListener[java.lang.Integer, ByteBuffer] {
    override def onRemoval(notification: RemovalNotification[Integer, ByteBuffer]): Unit =
      println("removed " + notification.getKey)
  })
    .build(new CacheLoader[java.lang.Integer, ByteBuffer] {
    def load(key: java.lang.Integer): ByteBuffer = {
      //      println(slotsSize + key.toLong * pageSize)
      println("loaded " + key)
      //      val res = new Array[Byte](pageSize)
      //      raf.seek(slotsSize + key.toLong * pageSize)
      //      raf.read(res)
      val res = raf.getChannel.map(FileChannel.MapMode.READ_ONLY, slotsSize + key.toLong * pageSize, pageSize)
      res
    }
  })

  object Slots {
    private val slots = new Array[Byte](slotsSize)
    if (file.length() > 0) {
      raf.read(slots)
    } else {
      raf.write(slots)
    }

    private def slotOf(lat: Int, lng: Int): Int = ((lng + 180) * 180 + 90 + lat) * 8

    def getSlot(lat: Int, lng: Int): Slot = getSlot(slotOf(lat, lng))

    private def getSlot(p: Int): Slot = {
      def getInt(p: Int): Int = {
        (slots(p) << 24) + ((slots(p + 1) & 0xff) << 16) + ((slots(p + 2) & 0xff) << 8) + ((slots(p + 3) & 0xff) << 0)
      }
      Slot((getInt(p).toLong << 32) + (getInt(p + 4) & 0xFFFFFFFFL))
    }

    def setSlot(lat: Int, lng: Int, slot: Slot): slot.type = setSlot(slotOf(lat, lng), slot)

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
      println("write slot " + p)
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

  def importTileTimed(lat: Int, lng: Int): Slot = {
    val a = System.currentTimeMillis
    val res = importTile(lat, lng)
    val b = System.currentTimeMillis
    if (b - a > 3)
      println("load " + (b - a))
    res
  }

  def importTile(lat: Int, lng: Int): Slot = {
    sourceProvider.sourceFor(lat, lng) match {
      case None => setSlot(lat, lng, new TimestampSlot(System.currentTimeMillis))
      case Some(sp) => doImportTile(lat, lng, sp)
    }
  }

  def doImportTile(lat: Int, lng: Int, source: PixelSource): PosSlot = {
    val newSlot = getSlot(lat, lng).posOrElse(raf.length())
    val b = System.currentTimeMillis
    setData(newSlot, source)
    val c = System.currentTimeMillis
    val res = setSlot(lat, lng, newSlot)
    println("import tile " + (c - b))
    res
  }

  def setData(p: PosSlot, value: PixelSource) = {
    val data = raf.getChannel.map(FileChannel.MapMode.READ_WRITE, p.value, pageSize)
    val a = System.currentTimeMillis
    //        for (x <- 0 to resolution) {
    //      for (y <- 0 to resolution) {
    //        val pixel = value.getPixel(x, y)
    //        data.put(dataPos(x, y), (pixel >> 8).toByte)
    //        data.put(dataPos(x, y) + 1, pixel.toByte)
    //      }
    //    }

    var y = 0
    while (y < resolution + 1) {
      value.doWithPixels(y, (x, pixel) => {
        val pos = dataPos(x, y)
        data.putShort(pos, pixel)
      })
      y += 1
    }

    val b = System.currentTimeMillis


    //    println("write map " + p.value)
    //    raf.seek(p.value)
    //    raf.getChannel.write()
    //    raf.write(data)
    //    val data = new Array[Byte](pageSize)
    //    for (x <- 0 to resolution) {
    //      for (y <- 0 to resolution) {
    //        val pixel = value.getPixel(x, y)
    //        data(dataPos(x, y)) = (pixel >> 8).toByte
    //        data(dataPos(x, y) + 1) = pixel.toByte
    //      }
    //    }
    //    val b = System.currentTimeMillis
    //    //    println("write map " + p.value)
    //    raf.seek(p.value)
    //    raf.write(data)
    val c = System.currentTimeMillis

    println("set data " + (b - a) + "; " + (c - b))
  }

  def dataPos(x: Int, y: Int) = (x + (resolution + 1) * y) * 2

  def getPixel(p: LatLng): Short = getPixel(p.lat, p.lng)

  def getPixel(lat: Double, lng: Double): Short = {
    def intPart(v: Double) = Math.floor(v).toInt
    val slat = intPart(lat)
    val slng = intPart(lng)
    getPixel(slat, slng, (resolution * (lng - slng)).toInt, (resolution * (1 - lat + slat)).toInt)
  }

  def getPixel(lat: Int, lng: Int, x: Int, y: Int): Short = {
    def eval(slot: Slot): Short = slot match {
      case s: PosSlot =>
        if (useCache) {
          val pageIndex = ((s.value - slotsSize) / pageSize).toInt
          val page = cache.get(pageIndex)
          page.getShort(dataPos(x, y))

          //                  ((page(dataPos(x, y)) << 8) + page(dataPos(x, y))).toShort
          //                  println("read " + pageIndex + "," + dataPos(x, y))
        } else {
          raf.seek(s.value + dataPos(x, y))
          raf.readShort()
        }
      case s: TimestampSlot =>
        if (s.value < minTime) eval(importTileTimed(lat, lng)) else -1000
    }
    eval(getSlot(lat, lng))
  }

  def getTile(lat: Int, lng: Int): Option[Tile] = {
    getSlot(lat, lng) match {
      case s: PosSlot =>
        val a = System.currentTimeMillis
        val pageIndex = ((s.value - slotsSize) / pageSize).toInt
        val buf = raf.getChannel.map(FileChannel.MapMode.READ_ONLY, slotsSize + pageIndex.toLong * pageSize, pageSize)
        val b = System.currentTimeMillis
        println("getTile " + (b - a))
        Some(new Tile(buf, resolution))
      case s: TimestampSlot => None
    }
  }

  def close() = raf.close()
}

class Tile(buffer: ByteBuffer, val resolution: Int) {
  def dataPos(x: Int, y: Int) = (x + (resolution + 1) * y) * 2

  def getPixel(x: Int, y: Int): Short = buffer.getShort(dataPos(x, y))

  def doWithPixels(y: Int, scale: Int, work: (Int, Short) => Unit): Unit = {
    var x = 0
    var pos = dataPos(x, y)
    while (x < resolution / scale + 1) {
      work(x, buffer.getShort(pos))
      x += scale
      pos += 2 * scale
    }
  }
}