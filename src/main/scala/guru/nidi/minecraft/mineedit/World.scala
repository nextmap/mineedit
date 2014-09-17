package guru.nidi.minecraft.mineedit

import guru.nidi.minecraft.mineedit.Tag._
import guru.nidi.minecraft.mineedit.Util.{floorDiv, floorMod}

import scala.collection.mutable

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
  def chunkPos(x: Int, z: Int) = floorDiv(x, 16) + floorDiv(z, 16) * 32

  def getChunk(x: Int, z: Int): Option[Chunk] = {
    Option(chunks(chunkPos(x, z)))
  }

  def setChunk(x: Int, z: Int, c: Chunk): Chunk = {
    chunks(chunkPos(x, z)) = c
    c
  }

  def getBlock(x: Int, y: Int, z: Int): Block = {
    getChunk(x, z) match {
      case None => Air()
      case Some(c) => c.getBlock(floorMod(x, 16), y, floorMod(z, 16))
    }
  }

  def setBlock(x: Int, y: Int, z: Int, b: Block): Unit = {
    (getChunk(x, z) match {
      case None => setChunk(x, z, new Chunk(x, z))
      case Some(c) => c
    }).setBlock(floorMod(x, 16), y, floorMod(z, 16), b)
  }

  override def equals(obj: scala.Any): Boolean =
    obj.isInstanceOf[Region] && obj.asInstanceOf[Region].chunks.sameElements(chunks)
}

class Chunk(val timestamp: Int, val root: CompoundTag) {
  def this(timestamp: Int, data: Array[Byte]) =
    this(timestamp, NbtReader.read(data).asInstanceOf[CompoundTag])

  def this(x: Int, z: Int) = this((System.currentTimeMillis() / 1000).toInt,
    new CompoundTag("", new CompoundTag("Level",
      ByteTag("V", 1),
      IntTag("xPos", floorDiv(x, 16)),
      IntTag("zPos", floorDiv(z, 16)),
      LongTag("LastUpdate", 0),
      ByteTag("LightPopulated", 0),
      ByteTag("TerrainPopulated", 0),
      LongTag("InhabitedTime", 0),
      IntArrayTag("HeightMap", new Array[Int](256)),
      ByteArrayTag("Biomes", new Array[Byte](256)),
      ListTag("Entities", 0, mutable.Buffer()),
      ListTag("TileEntities", 0, mutable.Buffer()),
      ListTag("Sections", 10, mutable.Buffer())
    )))

  def level: CompoundTag = root[CompoundTag]("Level")

  def xPos: Int = level[IntTag]("xPos")

  def zPos: Int = level[IntTag]("zPos")

  def sections: ListTag[CompoundTag] = level[ListTag[CompoundTag]]("Sections")

  def getSection(y: Int): Option[Section] =
    sections.find(ct => ct[ByteTag]("Y") - y / 16 == 0).map(new Section(_))

  def setSection(y: Int, s: Section): Section = {
    sections.add(s.root)
    s
  }

  def getBlock(x: Int, y: Int, z: Int): Block = {
    getSection(y) match {
      case None => Air()
      case Some(s) => s.getBlock(x, y, z)
    }
  }

  def setBlock(x: Int, y: Int, z: Int, b: Block): Unit = {
    (getSection(y) match {
      case None => setSection(y, new Section(y))
      case Some(s) => s
    }).setBlock(x, y, z, b)
  }

  override def equals(obj: scala.Any): Boolean =
    if (!obj.isInstanceOf[Chunk]) false
    else {
      val that = obj.asInstanceOf[Chunk]
      that.timestamp == timestamp && that.root == root
    }

  override def toString: String = s"Chunk at ($xPos,$zPos)"
}

class Section(val root: CompoundTag) {
  def this(y: Int) = this(new CompoundTag("",
    ByteTag("Y", (y / 16).toByte),
    ByteArrayTag("Blocks", new Array[Byte](4096)),
    ByteArrayTag("Data", new Array[Byte](2048)),
    ByteArrayTag("BlockLight", new Array[Byte](2048)),
    ByteArrayTag("SkyLight", Array.fill[Byte](2048)(136.toByte))
  ))

  def blocks = root[ByteArrayTag]("Blocks")

  def datas = root[ByteArrayTag]("Data")

  def getBlock(x: Int, y: Int, z: Int): Block = {
    val pos = blockPos(x, y, z)
    val id = blocks(pos)
    val data = datas(pos / 2)
    Block(id, if (x % 2 == 1) data >> 4 else data & 0xf)
  }

  def setBlock(x: Int, y: Int, z: Int, b: Block): Unit = {
    val pos = blockPos(x, y, z)
    blocks.set(pos, b.id)
    val data = datas(pos / 2)
    val newData = if (x % 2 == 1) (data & 0x0f) + (b.data << 4) else (data & 0xf0) + b.data
    datas.set(pos / 2, newData)
  }

  def blockPos(x: Int, y: Int, z: Int): Int = x + (y % 16) * 16 * 16 + z * 16

}