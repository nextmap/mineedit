package guru.nidi.minecraft.mineedit

import java.io.File
import java.nio.file.Files
import java.util.zip.Inflater

import guru.nidi.minecraft.mineedit.Util.{readByte, readInt}


/**
 *
 */
object WorldSaver {
  def save(dir: File, world: World): Unit = {
    world.regions.foreach(r => saveRegion(r._1, r._2))
  }

  private def saveRegion(position: XZ, region: Region): Unit = {

  }

  private def filenameOf(position: XZ): String = {
    s"r.${position.x}.${position.z}.mca"
  }

  private def loadRegion(file: File): Region = {
    val data = Files.readAllBytes(file.toPath)
    val chunks = new Array[Chunk](1024)
    for (i <- 0 until 1024) {
      val timestamp = readInt(data, i * 8)
      val pos = (readInt(data, i * 4) >> 8) * 4096
      if (timestamp != 0 && pos != 0) {
        chunks(i) = new Chunk(timestamp, readData(data, pos))
      }
    }
    new Region(chunks)
  }


  private def readData(data: Array[Byte], pos: Int): Array[Byte] = {
    val len = readInt(data, pos)
    val compression = readByte(data, pos + 4)
    if (compression != 2) throw new IllegalArgumentException("Only compression mode 2 is supported")
    val inflater = new Inflater()
    inflater.setInput(data, pos + 5, len)
    val raw = new Array[Byte](1000000)
    val size = inflater.inflate(raw)
    if (size == raw.length) throw new RuntimeException("too small raw buffer")
    val res = new Array[Byte](size)
    System.arraycopy(raw, 0, res, 0, size)
    res
  }
}
