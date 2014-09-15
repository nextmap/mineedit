package guru.nidi.minecraft.mineedit

import guru.nidi.minecraft.mineedit.Tag._
import guru.nidi.minecraft.mineedit.Util.{floorDiv, floorMod}

/**
 *
 */
class World(regions: Map[XZ, Region]) {
  def region(rx: Int, rz: Int): Option[Region] = regions.get(XZ(rx, rz))

  def getBlock(x: Int, y: Int, z: Int): Block = {
    region(floorDiv(x, 512), floorDiv(z, 512)) match {
      case None => Air()
      case Some(r) => r.getBlock(floorMod(x, 512), y, floorMod(z, 512))
    }
  }
}

object World {

}

case class XZ(x: Int, z: Int)

class Region(chunks: Array[Chunk]) {


  //  for (x1 <- 0 until 32)
  //    for (y1 <- 0 until 32)
  //      if (chunk(x1, y1).isDefined)
  //        println(x + "," + y + " " + chunk(x, y))

  def chunk(x: Int, z: Int): Option[Chunk] = {
    Option(chunks(x + z * 32))
  }

  def getBlock(x: Int, y: Int, z: Int): Block = {
    for (i <- 0 until 1024)
      if (chunks(i) != null)
        println(i + " " + chunks(i))
    chunk(floorDiv(x, 16), floorDiv(z, 16)) match {
      case None => Air()
      case Some(c) => c.getBlock(floorMod(x, 16), y, floorMod(z, 16))
    }
  }
}

class Chunk(val timestamp: Int, data: Array[Byte]) {
  val nbt = NbtParser.parse(data).asInstanceOf[CompoundTag]

  def root: CompoundTag = nbt[CompoundTag]("Level")

  def xPos: Int = root[IntTag]("xPos")

  def zPos: Int = root[IntTag]("zPos")

  def sections(y: Int): Option[CompoundTag] =
    root[ListTag[CompoundTag]]("Sections").find(ct => ct[ByteTag]("Y") - y == 0)

  def getBlock(x: Int, y: Int, z: Int): Block = {
    sections(y / 16) match {
      case None => Air()
      case Some(ct) =>
        val pos = x + (y % 16) * 16 * 16 + z * 16
        val id = ct[ByteArrayTag]("Blocks")(pos)
        val data = ct[ByteArrayTag]("Data")(pos / 2)
        Block(id, if (x % 2 == 1) data >> 4 else data & 0xf)
    }
  }

  override def toString: String = "Chunk at (" + xPos + "," + zPos + ")"
}