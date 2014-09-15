package guru.nidi.minecraft.mineedit

import java.io.{File, RandomAccessFile}
import java.util.zip.Inflater


/**
 *
 */
object WorldLoader {
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
    val in = new RandomAccessFile(file, "r")
    val chunks = new Array[Chunk](1024)
    for (i <- 0 until 1024) {
      in.seek(i * 4)
      val pos = (in.readInt() >> 8) << 12
      in.seek(4096 + i * 4)
      val timestamp = in.readInt()
      if (timestamp != 0 && pos != 0) {
        in.seek(pos)
        chunks(i) = new Chunk(timestamp, readData(in))
      }
    }
    in.close()
    new Region(chunks)
  }


  private def readData(in: RandomAccessFile): Array[Byte] = {
    val len = in.readInt()
    val compression = in.readByte()
    if (compression != 2) throw new IllegalArgumentException("Only compression mode 2 is supported")
    val inflater = new Inflater()
    val sourceData = new Array[Byte](len - 1)
    in.read(sourceData)
    inflater.setInput(sourceData)
    val raw = new Array[Byte](1000000)
    val size = inflater.inflate(raw)
    inflater.end()
    if (size == raw.length) throw new RuntimeException("too small raw buffer")
    val res = new Array[Byte](size)
    System.arraycopy(raw, 0, res, 0, size)
    res
  }
}
