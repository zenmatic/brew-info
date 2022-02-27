package brewinfo

import java.io.File
import scala.io.Source
import scala.io.StdIn
import scala.util.Random

import scala.collection.mutable.ListBuffer
import scala.collection.mutable.Map

object BrewLib {
        val formulaDir = "/usr/local/Homebrew/Library/Taps/homebrew/homebrew-core/Formula"
        val casksDir = "/usr/local/Homebrew/Library/Taps/homebrew/homebrew-cask/Casks"
        val random = new Random

        def getName(file: String): String =
                file.dropRight(".rb".length).split("/").last

        def getDescription(formulaName: String): String = {
                var desc = ""
                val bdirs = List(formulaDir, casksDir)
                for (bdir <- bdirs) {
                        val file = bdir + "/" + formulaName + ".rb"
                        try {
                                val lines = Source.fromFile(file).getLines().toList
                                val rawdesc = lines.map(_.trim).filter(_.startsWith("desc")).head.drop("desc ".length)
				desc += rawdesc.filter(_ != '"')
                        }
                        catch {
                                case e: java.io.FileNotFoundException => ""
                                case e: java.util.NoSuchElementException => ""
                        }
                }
                desc
        }

        def fixUpdate(): Unit = {
                var line = ""
		var formulas = Map[String, String]()
		var maxLen = 0
                while ({line = StdIn.readLine(); line != null}) {
                        val elements = line.split(" ")
                        if (elements.length == 1) {
                                val name = elements(0).trim
                                val desc = getDescription(name)
				formulas(name) = desc
				if (name.length > maxLen) {
					maxLen = name.length
				}
                        } else {
				for ((name, rawdesc) <- formulas) {
					val space = " " * (2 + maxLen - name.length)
					val desc2 = rawdesc.filter(_ != '"')
					println(f"$name$space$desc2")
				}
				formulas = Map[String, String]()
				maxLen = 0
				println()
				println(line)
                        }
                }
        }

        def getRandomElement[A](list: List[A]): A =
                list(random.nextInt(list.length))

        /*
        def getRandomElements[A](list: List[A], random: Random, max: Int): List[A] = {
                var seen = new ListBuffer[A]()
                for (i <- 1 to max) seen += getRandomElement(list, random)
                seen.toList
        }
        */

        def getListOfFiles(dir: String):List[File] = {
                val d = new File(dir)
                if (d.exists && d.isDirectory) {
                        d.listFiles.filter(_.isFile).toList
                } else {
                        List[File]()
                }
        }

        def displayRandomFormulas(): Unit = {
                var formulas = Map[String, String]()
                val formulaDirs = List(formulaDir, casksDir)
                for (formulaDir <- formulaDirs) {
                        val files = getListOfFiles(formulaDir).filter(_.getName.endsWith(".rb"))
                        for (file <- files) {
                                try {
                                        val name = file.getName.dropRight(".rb".length).split("/").last
                                        val lines = Source.fromFile(file).getLines().toList
                                        val desc = lines.map(_.trim).filter(_.startsWith("desc")).head.drop("desc ".length)
                                        formulas += (name -> desc)
                                }
                                catch {
                                        case e: java.io.FileNotFoundException => ""
                                        case e: java.util.NoSuchElementException => ""
                                }
                        }
                }

		var selectedFormulas = Map[String, String]()
		var maxLen = 0
                for (i <- 1 to 10) {
                        val name = BrewLib.getRandomElement(formulas.keys.toList)
			selectedFormulas(name) = formulas(name)
			if (name.length > maxLen) {
				maxLen = name.length
			}
                }

		for ((name, rawdesc) <- selectedFormulas.toSeq.sortBy(_._1)) {
			val space = " " * (2 + maxLen - name.length)
			val desc = rawdesc.filter(_ != '"')
                        println(f"$name$space$desc")
		}
        }
}
