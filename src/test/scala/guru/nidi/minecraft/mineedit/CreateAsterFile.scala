package guru.nidi.minecraft.mineedit

import java.io.File

/**
 *
 */
object CreateAsterFile {
  def main(args: Array[String]) {
    val asterFile = new AsterFile(new File("/Volumes/MY-HD-1/aster"))
    asterFile.importTiffs(new File("/Volumes/MY-HD-1/aster"))
    asterFile.close()
  }
}
