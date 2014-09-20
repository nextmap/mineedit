package guru.nidi.minecraft.mineedit

import java.io.File

/**
 *
 */
object CreateAsterFile {
  def main(args: Array[String]) {
    val asterFile = new AsterFile(new File("/Users/nidi/Downloads/aster/aster.ast"))
    asterFile.importTiffs(new File("/Users/nidi/Downloads/aster"))
    asterFile.close()
  }
}
