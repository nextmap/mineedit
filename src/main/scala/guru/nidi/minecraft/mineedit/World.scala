package guru.nidi.minecraft.mineedit

import guru.nidi.minecraft.mineedit.Tag._
import guru.nidi.minecraft.mineedit.Util.{floorDiv, floorMod}

/**
 *
 */
class World(val regions: Map[XZ, Region]) {
  def region(rx: Int, rz: Int): Option[Region] = regions.get(XZ(rx, rz))

  def getBlock(x: Int, y: Int, z: Int): Block = {
    region(floorDiv(x, 512), floorDiv(z, 512)) match {
      case None => Air()
      case Some(r) => r.getBlock(floorMod(x, 512), y, floorMod(z, 512))
    }
  }

  def setBlock(x: Int, y: Int, z: Int, b: Block): Unit = {
    region(floorDiv(x, 512), floorDiv(z, 512)) match {
      case None => throw new IllegalStateException(s"No region at ($x,$y,$z)")
      case Some(r) => r.setBlock(floorMod(x, 512), y, floorMod(z, 512), b)
    }
  }

  override def equals(obj: scala.Any): Boolean =
    obj.isInstanceOf[World] && obj.asInstanceOf[World].regions == regions
}

object World {

}

case class XZ(x: Int, z: Int)

class Region(val chunks: Array[Chunk]) {

  //  for (x1 <- 0 until 32)
  //    for (y1 <- 0 until 32)
  //      if (chunk(x1, y1).isDefined)
  //        println(x + "," + y + " " + chunk(x, y))

  def chunk(x: Int, z: Int): Option[Chunk] = {
    Option(chunks(x + z * 32))
  }

  def getBlock(x: Int, y: Int, z: Int): Block = {
    //    for (i <- 0 until 1024)
    //      if (chunks(i) != null)
    //        println(i + " " + chunks(i))
    chunk(floorDiv(x, 16), floorDiv(z, 16)) match {
      case None => Air()
      case Some(c) => c.getBlock(floorMod(x, 16), y, floorMod(z, 16))
    }
  }

  def setBlock(x: Int, y: Int, z: Int, b: Block): Unit = {
    chunk(floorDiv(x, 16), floorDiv(z, 16)) match {
      case None => throw new IllegalStateException(s"No chunk at ($x,$y,$z)")
      case Some(c) => c.setBlock(floorMod(x, 16), y, floorMod(z, 16), b)
    }
  }

  override def equals(obj: scala.Any): Boolean =
    obj.isInstanceOf[Region] && obj.asInstanceOf[Region].chunks.sameElements(chunks)
}

class Chunk(val timestamp: Int, val data: Array[Byte]) {
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
        val pos = blockPos(x, y, z)
        val id = ct[ByteArrayTag]("Blocks")(pos)
        val data = ct[ByteArrayTag]("Data")(pos / 2)
        Block(id, if (x % 2 == 1) data >> 4 else data & 0xf)
    }
  }

  def setBlock(x: Int, y: Int, z: Int, b: Block): Unit = {
    def createSection = CompoundTag("", Map(
      "Y" -> ByteTag("Y", (y % 16).toByte),
      "Blocks" -> ByteArrayTag("Blocks", new Array[Byte](4096)),
      "Data" -> ByteArrayTag("Data", new Array[Byte](2048))
    ))

    val section: CompoundTag = sections(y / 16) match {
      case None =>
        val newSec = createSection
        root[ListTag[CompoundTag]]("Sections").add(newSec)
        newSec
      case Some(ct) => ct
    }

    val pos = blockPos(x, y, z)
    section[ByteArrayTag]("Blocks").set(pos, b.id)
    val data = section[ByteArrayTag]("Data")(pos / 2)
    section[ByteArrayTag]("Data").set(pos / 2, if (x % 2 == 1) (data & 0x0f) + (b.data << 4) else (data & 0xf0) + b.data)
  }


  def blockPos(x: Int, y: Int, z: Int): Int = x + (y % 16) * 16 * 16 + z * 16


  override def equals(obj: scala.Any): Boolean =
    if (!obj.isInstanceOf[Chunk]) false
    else {
      val that = obj.asInstanceOf[Chunk]
      that.timestamp == timestamp && that.nbt == nbt
    }

  override def toString: String = s"Chunk at ($xPos,$zPos)"
}