package hu.andrasszatmari.goosegame

abstract class IO {
  def readCommand(): String
  def writeLine(line: String): Unit
  def showCursor(): Unit
}

class StdInIO extends IO {
  override def readCommand(): String = scala.io.StdIn.readLine()
  override def writeLine(line: String): Unit = println(line)
  override def showCursor(): Unit = print("type your command > ")
}

