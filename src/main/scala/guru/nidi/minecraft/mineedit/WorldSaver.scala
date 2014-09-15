package guru.nidi.minecraft.mineedit

import java.io.{File, RandomAccessFile}
import java.util.zip.Deflater


/**
 *
 */
object WorldSaver {
  def save(dir: File, world: World): Unit = {
    world.regions.foreach(r => saveRegion(new File(dir, "region/" + filenameOf(r._1)), r._2))
  }

  private def saveRegion(file: File, region: Region): Unit = {
    file.getParentFile.mkdirs()
    val out = new RandomAccessFile(file, "rw")

    def writeHeader(i: Int, pos: Int, timestamp: Int) = {
      out.seek(i * 4)
      out.writeInt(pos)
      out.seek(4096 + i * 4)
      out.writeInt(timestamp)
    }

    var pos = 8192
    for (i <- 0 until 1024) {
      val chunk = region.chunks(i)
      if (chunk != null) {
        out.seek(pos)
        val size = writeData(out, chunk.data)
        writeHeader(i, (pos >> 12 << 8) + (size >> 12), chunk.timestamp)
        pos += size
      } else {
        writeHeader(i, 0, 0)
      }
    }
    out.close()
  }

  private def filenameOf(position: XZ): String = {
    s"r.${position.x}.${position.z}.mca"
  }

  private def writeData(out: RandomAccessFile, data: Array[Byte]): Int = {
    val deflater = new Deflater()
    deflater.setInput(data)
    deflater.finish()
    val raw = new Array[Byte](1000000)
    val size = deflater.deflate(raw)
    deflater.end()
    val mod = size % 4096
    val blockSize = if (mod == 0) size else size + 4096 - mod
    if (blockSize >= raw.length) throw new RuntimeException("too small raw buffer")
    out.writeInt(size + 1)
    out.writeByte(2)
    out.write(raw, 0, blockSize)
    blockSize
  }
}
