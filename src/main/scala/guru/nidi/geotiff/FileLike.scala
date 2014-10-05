package guru.nidi.geotiff

/**
 *
 */
trait FileLike {
  def close(): Unit

  def seek(pos: Long): Unit

  def getPos: Long

  def read(buf: Array[Byte]): Unit

  def readShort(): Short

  def readInt(): Int

  def readLong(): Long
}
