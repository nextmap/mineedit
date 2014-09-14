package guru.nidi.minecraft.mineedit

/**
 *
 */
class World(regions: Map[XZ, Region]) {
}

object World {

}

case class XZ(x: Int, z: Int)

class Region(chunks: Array[Chunk]) {}

class Chunk(val timestamp: Int, data: Array[Byte]) {
  val nbt = NbtParser.parse(data)
//  println(timestamp, nbt)
}