package guru.nidi.geotiff

import java.io.File

import scala.annotation.tailrec
import scala.collection.mutable

/**
 *
 */
object GeoTiffReader {
  def read(file: File): GeoTiff = {
    new GeoTiffReader(file).geoTiff
  }
}

private class GeoTiffReader(file: File) {
  val in = new EndianAwareRandomAccessFile(file, "r")

  var width = 0
  var height = 0
  var bitsPerSample = 0
  val bytesPerPixel = 2
  val info = mutable.Map[String, String]()
  var stripOffsets = Array[Int]()
  var rowsPerStrip = 0
  var stripByteCounts = Array[Int]()
  var xResolution = 0.0
  var yResolution = 0.0
  var resolutionUnit = 2
  var modelPixelScale = Array[Double](1, 1, 1)
  var modelTiepoints = Array[Double]()
  var geoKeyDirectory = Array[Int]()
  var strips: Array[Array[Byte]] = null

  val geoTiff = {
    val endian = in.readShort()
    if (endian == 0x4949) in.setLittleEndian()
    else if (endian == 0x4d4d) in.setBigEndian()
    else throw new IllegalArgumentException(s"Invalid endian $endian")

    val magic = in.readShort()
    if (magic != 42) throw new IllegalArgumentException(s"Invalid magic number $magic")

    readDirectory()
    in.close()

    new GeoTiff(this, width, height, bitsPerSample, info,
      xResolution, yResolution, resolutionUnit,
      modelPixelScale, modelTiepoints, geoKeyDirectory)
  }

  @tailrec
  private def readDirectory(): Unit = {
    val pos = in.readInt()
    if (pos != 0) {
      in.seek(pos)
      val len = in.readShort()
      for (i <- 0 until len) {
        readEntry()
      }
      in.doAt(0) {
        readStrips()
      }
      readDirectory()
    }
  }

  private def readEntry() {
    val tag = in.readShort()
    val typ = in.readShort()
    val len = in.readInt()
    val pos = in.readInt()

    def readScalar(): Int = {
      if (typ != 1 && typ != 3 && typ != 4) throw new UnsupportedOperationException(s"Unknown type $typ")
      pos
    }

    def readString(): String = {
      if (typ != 2) throw new UnsupportedOperationException(s"Unknown type $typ")
      in.doAt(pos) {
        in.readAscii(len - 1)
      }
    }

    def readFrac(): Double = {
      if (typ != 5) throw new UnsupportedOperationException(s"Unknown type $typ")
      in.doAt(pos) {
        in.readInt().toDouble / in.readInt()
      }
    }

    def readDoubles(): Array[Double] = {
      if (typ != 12) throw new UnsupportedOperationException(s"Unknown type $typ")
      in.doAt(pos) {
        val res = new Array[Double](len)
        for (i <- 0 until len) res(i) = in.readDouble()
        res
      }
    }

    def readScalars(): Array[Int] = {
      if (typ != 1 && typ != 3 && typ != 4) throw new UnsupportedOperationException(s"Unknown type $typ")
      in.doAt(pos) {
        val res = new Array[Int](len)
        for (i <- 0 until len) res(i) = typ match {
          case 3 => in.readShort()
          case 4 => in.readInt()
        }
        res
      }
    }


    tag match {
      case 256 => width = readScalar()
      case 257 => height = readScalar()
      case 258 => bitsPerSample = readScalar()
      case 259 => if (readScalar() != 1) throw new UnsupportedOperationException(s"Unknown compression")
      case 262 => //ignore photometric interpretation
      case 266 => if (readScalar() != 1) throw new UnsupportedOperationException(s"Unknown fill order")
      case 269 => info.put("documentName", readString())
      case 270 => info.put("imageDescription", readString())
      case 273 => stripOffsets = readScalars()
      case 274 => //ignore orientation
      case 277 => if (readScalar() != 1) throw new UnsupportedOperationException(s"Unknown samples per pixel")
      case 278 => rowsPerStrip = readScalar()
      case 279 => stripByteCounts = readScalars()
      case 282 => xResolution = readFrac()
      case 283 => yResolution = readFrac()
      case 284 => if (readScalar() != 1) throw new UnsupportedOperationException(s"Unknown planar configuration")
      case 296 => resolutionUnit = readScalar()
      case 305 => info.put("software", readString())
      case 306 => info.put("dateTime", readString())
      case 339 => if (readScalar() != 2) throw new UnsupportedOperationException(s"Unknown sample format")
      case -31986 => modelPixelScale = readDoubles()
      case -31614 => modelTiepoints = readDoubles()
      case -30801 => geoKeyDirectory = readScalars()
    }
  }

  private def readStrips() = {
    val stripCount = height / rowsPerStrip
    strips = new Array[Array[Byte]](stripCount)
    for (s <- 0 until stripCount) {
      in.seek(stripOffsets(s))
      strips(s) = in.read(rowsPerStrip * width * bytesPerPixel)
    }
  }

  def getPixel(x: Int, y: Int): Int = {
    val s = y / rowsPerStrip
    val pos = (x + (y % rowsPerStrip) * width) * bytesPerPixel
    in.readShort(strips(s), pos)
  }

}
