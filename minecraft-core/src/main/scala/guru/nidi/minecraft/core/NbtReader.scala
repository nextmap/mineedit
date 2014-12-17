package guru.nidi.minecraft.core

import java.io.{ByteArrayInputStream, DataInputStream}

import scala.collection.mutable

/**
 *
 */
object NbtReader {
  def read(data: Array[Byte]): Tag = {
    if (data == null) null
    else new NbtReader(data).parseTag()
  }
}

private class NbtReader(data: Array[Byte]) {
  private val in = new DataInputStream(new ByteArrayInputStream(data))

  def parseByteArray(): Array[Byte] = {
    val len = in.readInt()
    val value = new Array[Byte](len)
    in.read(value)
    value
  }

  def parseIntArray(): Array[Int] = {
    val len = in.readInt()
    val value = new Array[Int](len)
    for (i <- 0 until len) {
      value(i) = in.readInt()
    }
    value
  }

  def parseCompound(): collection.Map[String, Tag] = {
    val res = mutable.Map[String, Tag]()
    var tag = parseTag()
    while (!tag.isInstanceOf[EndTag]) {
      res.put(tag.name, tag)
      tag = parseTag()
    }
    res
  }

  def parseList[T <: Tag](id: Byte): mutable.Buffer[T] = {
    val res = mutable.Buffer[T]()
    val len = in.readInt()
    for (i <- 0 until len) {
      res += parseNamedTag(id, "").asInstanceOf[T]
    }
    res
  }

  def parseNamedTag(id: Byte, name: String): Tag = {
    id match {
      case 1 => ByteTag(name, in.readByte())
      case 2 => ShortTag(name, in.readShort())
      case 3 => IntTag(name, in.readInt())
      case 4 => LongTag(name, in.readLong())
      case 5 => FloatTag(name, in.readFloat())
      case 6 => DoubleTag(name, in.readDouble())
      case 7 => ByteArrayTag(name, parseByteArray())
      case 8 => StringTag(name, in.readUTF())
      case 9 => val id = in.readByte(); ListTag(name, id, parseList(id))
      case 10 => CompoundTag(name, parseCompound())
      case 11 => IntArrayTag(name, parseIntArray())
      case other => throw new IllegalArgumentException(s"unknown tag id $other")
    }
  }

  def parseTag(): Tag = {
    val id = in.readByte()
    if (id == 0) EndTag()
    else parseNamedTag(id, in.readUTF())
  }
}

