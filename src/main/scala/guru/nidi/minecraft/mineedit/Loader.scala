package guru.nidi.minecraft.mineedit

import java.io.File
import java.nio.file.Files
import java.util.zip.Inflater


/**
 *
 */
object Loader {
  def load(dir: File): World = {
    new World(
      new File(dir, "region").listFiles()
        .filter(_.getName.endsWith(".mca"))
        .map(f => (positionOf(f.getName), loadRegion(f)))
        .toMap
    )
  }

  private def positionOf(filename: String): XZ = {
    val secondDot = filename.indexOf('.', 2)
    val thirdDot = filename.indexOf('.', secondDot + 1)
    XZ(filename.substring(2, secondDot).toInt,
      filename.substring(secondDot + 1, thirdDot).toInt)
  }

  private def loadRegion(file: File): Region = {
    val data = Files.readAllBytes(file.toPath)
    val chunks = new Array[Chunk](1024)
    for (i <- 0 until 1024) {
      chunks(i) = new Chunk(
        Util.readInt(data, i * 8),
        readData(data, (Util.readInt(data, i * 4) >> 8) * 4096))
    }
    new Region(chunks)
  }


  private def readData(data: Array[Byte], pos: Int): Array[Byte] = {
    if (pos == 0) return null
    val len = Util.readInt(data, pos)
    val compression = Util.readByte(data,pos + 4)
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
