package guru.nidi.minecraft.mineedit

/**
 *
 */
object Util {
  def readLong(data: Array[Byte], pos: Int): Long =
    (readInt(data, pos).toLong << 32) + readInt(data, pos + 4)

  def readInt(data: Array[Byte], pos: Int): Int =
    ((data(pos) & 0xff) << 24) + ((data(pos + 1) & 0xff) << 16) + ((data(pos + 2) & 0xff) << 8) + ((data(pos + 3) & 0xff) << 0)

  def readShort(data: Array[Byte], pos: Int): Short =
    (((data(pos) & 0xff) << 8) + ((data(pos + 1) & 0xff) << 0)).toShort

  def readByte(data: Array[Byte], pos: Int): Byte =
    data(pos)

  def readFloat(data: Array[Byte], pos: Int): Float =
    java.lang.Float.intBitsToFloat(readInt(data, pos))

  def readDouble(data: Array[Byte], pos: Int): Double =
    java.lang.Double.longBitsToDouble(readLong(data, pos))

  def floorDiv(x: Int, y: Int): Int = {
    val r = x / y
    if ((x ^ y) < 0 && (r * y != x)) r - 1 else r
  }

  def floorMod(x: Int, y: Int): Int = x - floorDiv(x, y) * y

  def floorMod(x: Int, y: Int, wrap: Int): Int = {
    val m = x % y
    if (x < 0) wrap + m else m
  }
}
