package guru.nidi.minecraft.mineedit

import scala.collection.mutable

/**
 *
 */
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

object Tag {
  implicit def intTag2Int(tag: IntTag): Int = tag.value

  implicit def byteTag2Int(tag: ByteTag): Byte = tag.value

  implicit def byteArrayTag2Array(tag: ByteArrayTag): Array[Byte] = tag.value

  implicit def listTag2Seq[T <: Tag](tag: ListTag[T]): Seq[T] = tag.value
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

//TODO typeId should be type save
case class ListTag[T <: Tag](name: String, typeId: Byte, value: mutable.Buffer[T]) extends Tag {
  def add(tag: T): Unit = value += tag

  override def toString: String = {
    val values = value.mkString(",")
    s"$name=[$values]"
  }
}

case class CompoundTag(name: String, value: collection.Map[String, Tag]) extends Tag {
  def this(name: String, tags: Tag*) =
    this(name, tags.map(tag => (tag.name, tag)).toMap)

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

