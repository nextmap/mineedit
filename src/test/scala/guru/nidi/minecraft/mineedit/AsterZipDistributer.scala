package guru.nidi.minecraft.mineedit

import java.io.File
import java.nio.file.Files

/**
 *
 */
object AsterZipDistributer {
  def main(args: Array[String]) {
    val basedir = new File("/Volumes/MY-HD-1/aster")
    for (f <- basedir.listFiles().filter(f => f.getName.startsWith("ASTGTM2_"))) {
      val dest = new File(basedir, f.getName.substring(8, 11))
      dest.mkdir()
      try {
        Files.move(f.toPath, new File(dest, f.getName).toPath)
      } catch {
        case e: Exception => println(f.getName + ": " + e)
        case _ =>
      }
    }
  }
}
