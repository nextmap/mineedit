package guru.nidi.minecraft.mineedit

/**
 *
 */
trait CoordTransformer {
  def apply[T](world: World, model: Model[T], applyer: DataApplyer[T])

  def mine2model(x: Int, z: Int): LatLng
}

class LinearCoordTransformer(minePos: Xz, mineXlen: Int, model0: LatLng, model1: LatLng) extends CoordTransformer {
  val modelSize = model1 - model0
  val mineZlen = (mineXlen / modelSize.lng * modelSize.lat).toInt

  override def mine2model(x: Int, z: Int): LatLng = {
    (model0 + LatLng(
      modelSize.lat * (z - minePos.z) / mineZlen,
      modelSize.lng * (x - minePos.x) / mineXlen)).normalize
  }

  def apply[T](world: World, model: Model[T], applyer: DataApplyer[T]) = {
    val blockSize = 5
    val xmax = minePos.x + mineXlen
    val zmax = minePos.z + mineZlen
    for (xb <- minePos.x until xmax - 1 by blockSize) {
      for (zb <- minePos.z until zmax - 1 by blockSize) {
        println(s"($xb,$zb)")

        for (x <- xb until Math.min(xb + blockSize, xmax - 1)) {
          for (z <- zb until Math.min(zb + blockSize, zmax - 1)) {
            val modelCoord0 = mine2model(x, z)
            val modelCoord1 = mine2model(x + 1, z + 1)
            val data = model.getData(modelCoord0, modelCoord1)
            applyer.applyData(world, x, zmax - z, data)
          }
        }
      }
    }

  }

}