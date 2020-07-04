package brewinfo

import java.io.File
import scala.io.Source
import scala.util.Random
import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Map

object Main extends App {
    def getListOfFiles(dir: String):List[File] = {
        val d = new File(dir)
        if (d.exists && d.isDirectory) {
            d.listFiles.filter(_.isFile).toList
        } else {
            List[File]()
        }
    }

    def getRandomElement[A](list: List[A], random: Random): A =
        list(random.nextInt(list.length))

    /*
    def getRandomElements[A](list: List[A], random: Random, max: Int): List[A] = {
        var seen = new ListBuffer[A]()
        for (i <- 1 to max) seen += getRandomElement(list, random)
        seen.toList
    }
    */

    var formulas = Map[String, String]()

    val formulaDir = "/usr/local/Homebrew/Library/Taps/homebrew/homebrew-core/Formula"
    val files = getListOfFiles(formulaDir).filter(_.getName.endsWith(".rb"))
    for (file <- files) {
        val name = file.getName.dropRight(".rb".length).split("/").last
        val lines = Source.fromFile(file).getLines().toList
        val desc = lines.map(_.trim).filter(_.startsWith("desc")).head.drop("desc ".length)
        formulas += (name -> desc)
    }

    val random = new Random
    for (i <- 1 to 10) {
        val name = getRandomElement(formulas.keys.toList, random)
        val desc = formulas(name)
        println(f"$name%20s $desc")
    }
}
