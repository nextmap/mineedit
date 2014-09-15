package guru.nidi.minecraft.mineedit

import java.nio.charset.Charset

import guru.nidi.minecraft.mineedit.Util._

import scala.collection.mutable

/**
 *
 */
object NbtParser {
  def parse(data: Array[Byte]): Tag = {
    if (data == null) null
    else new NbtParser(data).parseTag()
  }
}

private class NbtParser(data: Array[Byte]) {
  private var pos = 0

  def parseByte(): Byte = {
    pos += 1
    readByte(data, pos - 1)
  }

  def parseShort(): Short = {
    pos += 2
    readShort(data, pos - 2)
  }

  def parseInt(): Int = {
    pos += 4
    readInt(data, pos - 4)
  }

  def parseLong(): Long = {
    pos += 8
    readLong(data, pos - 8)
  }

  def parseFloat(): Float = {
    pos += 4
    readFloat(data, pos - 4)
  }

  def parseDouble(): Double = {
    pos += 8
    readDouble(data, pos - 8)
  }

  def parseByteArray(): Array[Byte] = {
    val len = parseInt()
    val value = new Array[Byte](len)
    for (i <- 0 until len) {
      value(i) = parseByte()
    }
    value
  }

  def parseIntArray(): Array[Int] = {
    val len = parseInt()
    val value = new Array[Int](len)
    for (i <- 0 until len) {
      value(i) = parseInt()
    }
    value
  }

  def parseString(): String = {
    val len = parseShort()
    pos += len
    new String(data, pos - len, len, Charset.forName("utf-8"))
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

  def parseList[T <: Tag](): Seq[T] = {
    val res = mutable.Buffer[T]()
    val id = parseByte()
    val len = parseInt()
    for (i <- 0 until len) {
      res += parseNamedTag(id, "").asInstanceOf[T]
    }
    res
  }

  def parseNamedTag(id: Byte, name: String): Tag = {
    id match {
      case 1 => ByteTag(name, parseByte())
      case 2 => ShortTag(name, parseShort())
      case 3 => IntTag(name, parseInt())
      case 4 => LongTag(name, parseLong())
      case 5 => FloatTag(name, parseFloat())
      case 6 => DoubleTag(name, parseDouble())
      case 7 => ByteArrayTag(name, parseByteArray())
      case 8 => StringTag(name, parseString())
      case 9 => ListTag(name, parseList())
      case 10 => CompoundTag(name, parseCompound())
      case 11 => IntArrayTag(name, parseIntArray())
      case other => throw new IllegalArgumentException("unknown tag id " + other)
    }
  }

  def parseTag(): Tag = {
    val id = parseByte()
    if (id == 0) EndTag()
    else parseNamedTag(id, parseString())
  }
}

object Tag {
  implicit def intTag2Int(tag: IntTag): Int = tag.value

  implicit def byteTag2Int(tag: ByteTag): Byte = tag.value

  implicit def byteArrayTag2Array(tag: ByteArrayTag): Array[Byte] = tag.value

  implicit def listTag2Seq[T <: Tag](tag: ListTag[T]): Seq[T] = tag.value
}

abstract class Tag() {
  def name: String
}

case class EndTag() extends Tag {
  val name = ""

  override def toString: String = "|"
}

case class ByteTag(name: String, value: Byte) extends Tag {
  override def toString: String = name + "=" + value + "B"
}

case class ShortTag(name: String, value: Short) extends Tag {
  override def toString: String = name + "=" + value + "S"
}

case class IntTag(name: String, value: Int) extends Tag {
  override def toString: String = name + "=" + value
}

case class LongTag(name: String, value: Long) extends Tag {
  override def toString: String = name + "=" + value + "L"
}

case class FloatTag(name: String, value: Float) extends Tag {
  override def toString: String = name + "=" + value + "F"
}

case class DoubleTag(name: String, value: Double) extends Tag {
  override def toString: String = name + "=" + value + "D"
}

case class ByteArrayTag(name: String, value: Array[Byte]) extends Tag {
  override def toString: String = name + "=[B...]"
}

case class StringTag(name: String, value: String) extends Tag {
  override def toString: String = name + "='" + value + "'"
}

case class ListTag[T <: Tag](name: String, value: Seq[T]) extends Tag {
  override def toString: String = name + "=[" + value.mkString(",") + "]"
}

case class CompoundTag(name: String, value: collection.Map[String, Tag]) extends Tag {
  def get(name: String): Option[Tag] = value.get(name)

  def apply[T <: Tag](name: String): T = get(name).get.asInstanceOf[T]

  override def toString: String = name + "={" + value.values.mkString(",") + "}"
}

case class IntArrayTag(name: String, value: Array[Int]) extends Tag {
  override def toString: String = name + "=[I...]"
}

