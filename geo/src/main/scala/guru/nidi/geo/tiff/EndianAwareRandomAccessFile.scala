package guru.nidi.geo.tiff

import java.nio.charset.Charset

/**
 *
 */
class EndianAwareRandomAccessFile(file: FileLike) {
  var bigEndian = true

  def setBigEndian() = bigEndian = true

  def setLittleEndian() = bigEndian = false

  def close() = file.close()

  def seek(pos: Long) = {
    file.seek(pos)
  }

  def getPos: Long = file.getPos

  def doAt[T](pos: Long)(action: => T): T = {
    val oldPos = getPos
    seek(pos)
    val res = action
    seek(oldPos)
    res
  }

  def read(len: Int): Array[Byte] = {
    val v = new Array[Byte](len)
    file.read(v)
    v
  }

  def readShort(): Short = {
    val v = file.readShort()
    if (bigEndian) v
    else (((v >> 8) & 0xff) + (v << 8)).toShort
  }

  def readShort(data: Array[Byte], pos: Int): Short = {
    val v = ((data(pos) << 8) + data(pos + 1)).toShort
    if (bigEndian) v
    else (((v >> 8) & 0xff) + (v << 8)).toShort
  }

  def readInt(): Int = {
    val v = file.readInt()
    if (bigEndian) v
    else ((v >> 24) & 0xff) + ((v >> 8) & 0xff00) + ((v << 8) & 0xff0000) + (v << 24)
  }

  def readLong(): Long = {
    val v = file.readLong()
    if (bigEndian) v
    else ((v >> 56) & 0xff) + ((v >> 40) & 0xff00) + ((v >> 24) & 0xff0000) + ((v >> 8) & 0xff000000) +
      ((v << 8) & 0xff00000000L) + ((v << 24) & 0xff0000000000L) + ((v << 40) & 0xff000000000000L) + (v << 56)
  }

  def readDouble(): Double = {
    val v = readLong()
    java.lang.Double.longBitsToDouble(v)
  }

  def readAscii(len: Int): String = {
    val buf = new Array[Byte](len)
    file.read(buf)
    new String(buf, Charset.forName("us-ascii"))
  }
}
