package guru.nidi.geo.tiff

import java.io.RandomAccessFile

/**
 *
 */
class RandomAccessFileLike(raf: RandomAccessFile) extends FileLike {
  override def close(): Unit = raf.close()

  override def readLong(): Long = raf.readLong()

  override def readShort(): Short = raf.readShort()

  override def getPos: Long = raf.getFilePointer

  override def readInt(): Int = raf.readInt()

  override def seek(pos: Long): Unit = raf.seek(pos)

  override def read(buf: Array[Byte]): Unit = raf.read(buf)
}
