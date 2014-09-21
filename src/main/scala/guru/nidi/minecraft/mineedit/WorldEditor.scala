package guru.nidi.minecraft.mineedit

/**
 *
 */
class WorldEditor[T](coordTrans: CoordTransformer,
                     model: Model[T],
                     applyer: DataApplyer[T]) {

  def setElevation(world: World, p: Xz, xl: Int, zl: Int) = {
    for (x <- p.x until p.x + xl) {
      println(s"$x")
      for (z <- p.z until p.z + zl) {
        val modelCoord0 = coordTrans.mine2model((x - p.x) / xl.toDouble, (p.z + zl - 1 - z) / zl.toDouble)
        val modelCoord1 = coordTrans.mine2model((x - p.x + 1) / xl.toDouble, (p.z + zl - z) / zl.toDouble)
        val data = model.getData(modelCoord0, modelCoord1)
        applyer.applyData(world, x, z, data)
      }
    }
  }
}

object WorldEditor {
  def setElevation(world: World, p: Xz, xl: Int, zl: Int, editors: WorldEditor[_]*) = {
    editors.foreach(_.setElevation(world, p, xl, zl))
  }
}