package guru.nidi.geo.tiff

import java.io.{BufferedInputStream, InputStream}

/**
 *
 */
class BufferedFileLike(f: InputStream, size: Int) extends FileLike {
  val data = new Array[Byte](size)
  new BufferedInputStream(f).read(data)

  var pos = 0

  override def close(): Unit = {}

  override def readShort(): Short = {
    pos += 2
    (((data(pos - 2) & 0xff) << 8) + (data(pos - 1) & 0xff)).toShort
  }

  override def readInt(): Int = {
    pos += 4
    ((data(pos - 4) & 0xff) << 24) + ((data(pos - 3) & 0xff) << 16) +
      ((data(pos - 2) & 0xff) << 8) + ((data(pos - 1) & 0xff))
  }

  override def readLong(): Long = {
    (readInt().toLong << 32) + (readInt() & 0xFFFFFFFFL)
  }

  override def read(buf: Array[Byte]): Unit = {
    System.arraycopy(data, pos, buf, 0, buf.length)
    pos += buf.length
  }

  override def getPos: Long = pos

  override def seek(pos: Long): Unit = this.pos = pos.toInt

}
