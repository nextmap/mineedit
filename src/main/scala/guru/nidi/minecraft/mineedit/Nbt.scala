package guru.nidi.minecraft.mineedit

import java.io.{ByteArrayInputStream, DataInputStream}

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

  def parseList[T <: Tag](): mutable.Buffer[T] = {
    val res = mutable.Buffer[T]()
    val id = in.readByte()
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
      case 9 => ListTag(name, parseList())
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

object Tag {
  implicit def intTag2Int(tag: IntTag): Int = tag.value

  implicit def byteTag2Int(tag: ByteTag): Byte = tag.value

  implicit def byteArrayTag2Array(tag: ByteArrayTag): Array[Byte] = tag.value

  implicit def listTag2Seq[T <: Tag](tag: ListTag[T]): Seq[T] = tag.value
}

abstract class Tag() {
  def name: String

  def value: Any

  override def equals(obj: scala.Any): Boolean =
    if (obj.getClass != getClass) false
    else {
      val that = obj.asInstanceOf[Tag]
      that.name == name && that.value == value
    }
}

case class EndTag() extends Tag {
  val name = ""
  val value = null

  override def toString: String = "|"
}

case class ByteTag(name: String, value: Byte) extends Tag {
  override def toString: String = s"$name=${value}B"
}

case class ShortTag(name: String, value: Short) extends Tag {
  override def toString: String = s"$name=${value}S"
}

case class IntTag(name: String, value: Int) extends Tag {
  override def toString: String = s"$name=$value"
}

case class LongTag(name: String, value: Long) extends Tag {
  override def toString: String = s"$name=${value}L"
}

case class FloatTag(name: String, value: Float) extends Tag {
  override def toString: String = s"$name=${value}F"
}

case class DoubleTag(name: String, value: Double) extends Tag {
  override def toString: String = s"$name=${value}D"
}

case class ByteArrayTag(name: String, value: Array[Byte]) extends Tag {
  def set(pos: Int, value: Int) = this.value(pos) = value.toByte

  override def equals(obj: scala.Any): Boolean =
    if (obj.getClass != getClass) false
    else {
      val that = obj.asInstanceOf[ByteArrayTag]
      that.name == name && that.value.sameElements(value)
    }

  override def toString: String = s"$name=[${value.length} B]"
}

case class StringTag(name: String, value: String) extends Tag {
  override def toString: String = s"$name='$value'"
}

case class ListTag[T <: Tag](name: String, value: mutable.Buffer[T]) extends Tag {
  def add(tag: T): Unit = value += tag

  override def toString: String = {
    val values = value.mkString(",")
    s"$name=[$values]"
  }
}

case class CompoundTag(name: String, value: collection.Map[String, Tag]) extends Tag {
  def get(name: String): Option[Tag] = value.get(name)

  def apply[T <: Tag](name: String): T = get(name).get.asInstanceOf[T]

  override def toString: String = {
    val values = value.values.mkString(",")
    s"$name={$values}"
  }
}

case class IntArrayTag(name: String, value: Array[Int]) extends Tag {
  override def equals(obj: scala.Any): Boolean =
    if (obj.getClass != getClass) false
    else {
      val that = obj.asInstanceOf[IntArrayTag]
      that.name == name && that.value.sameElements(value)
    }

  override def toString: String = s"$name=[${value.length} I]"
}

