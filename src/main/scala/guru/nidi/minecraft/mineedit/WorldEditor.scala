package guru.nidi.minecraft.mineedit

/**
 *
 */
class WorldEditor[S, T](coordTrans: CoordTransformer,
                        model: Model[S],
                        dataTrans: DataTransformer[S, T],
                        applyer: DataApplyer[T]) {

  def setElevation(world: World, x0: Int, z0: Int, xl: Int, zl: Int) = {
    for (x <- x0 until x0 + xl) {
      println(s"$x")
      for (z <- z0 until z0 + zl) {
        val modelCoord0 = coordTrans.mine2model((x - x0) / xl.toDouble, (z - z0) / zl.toDouble)
        val modelCoord1 = coordTrans.mine2model((x - x0 + 1) / xl.toDouble, (z - z0 + 1) / zl.toDouble)
        val data = model.getData(modelCoord0._1, modelCoord0._2, modelCoord1._1 - modelCoord0._1, modelCoord1._2 - modelCoord0._2)
        applyer.applyData(world, x, z, dataTrans.model2mine(data))
      }
    }
  }
}

object WorldEditor {
  def setElevation(world: World, x0: Int, z0: Int, xl: Int, zl: Int, editors: WorldEditor[_, _]*) = {
    editors.foreach(_.setElevation(world, x0, z0, xl, zl))
  }
}