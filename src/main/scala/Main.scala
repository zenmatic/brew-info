package brewinfo

object Main extends App {
        if (args.length == 1) {
                val command = args(0)
                if (command == "update") {
                        BrewLib.fixUpdate()
                }
                if (command == "random") {
                        BrewLib.displayRandomFormulas()
                }
        }
}
