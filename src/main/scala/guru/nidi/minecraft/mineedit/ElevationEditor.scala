package guru.nidi.minecraft.mineedit

/**
 *
 */
class ElevationEditor(transformer: CoordTransformer, provider: ElevationProvider, applyer: ElevationApplyer) {
  def setElevation(world: World, x0: Int, z0: Int, xl: Int, zl: Int) = {
    for (x <- x0 until x0 + xl) {
      for (z <- z0 until z0 + zl) {
        println(s"$x,$z")
        val modelCoord = transformer.mine2model((x - x0) / xl.toDouble, (z - z0) / zl.toDouble)
        val elevation = provider.getElevation(modelCoord._1, modelCoord._2)
        applyer.applyElevation(world, x, transformer.model2mine(elevation), z)
      }
    }
  }
}
