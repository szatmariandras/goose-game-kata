# Goose Game Kata in Scala

Goose Game in Scala, based on https://github.com/xpeppers/goose-game-kata.

## How to run the game?

To run the game, you need to install the [Java Runtime Environment](https://www.java.com/en/download/).

The file `bin/goose-game.jar` is a compiled executable which works on Unix and Windows systems. On Windows, you might need to run the game by typing `java -jar bin/goose-game.jar`.

## How to change the code?

You need the [JDK 8 or higher](https://www.oracle.com/technetwork/java/javase/downloads/index.html) and [sbt](https://www.scala-sbt.org/) to compile the code, run the tests and create an executable uber-JAR.

To run the tests, run `$ sbt test`, or type `test` from the sbt console.

The game could be run directly by running `$ sbt run` from the shell, or `run` from the sbt console.

To update the executable uber-JAR, run `$ sbt assembly` or `assembly` from the sbt console.



  

