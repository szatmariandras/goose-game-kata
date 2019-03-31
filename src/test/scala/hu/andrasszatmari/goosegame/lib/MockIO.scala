package hu.andrasszatmari.goosegame.lib

import hu.andrasszatmari.goosegame.IO

import scala.collection.mutable

class MockIO(input: List[String] = List.empty) extends IO {

  private val queue = input.to[mutable.Queue]

  private var output = Vector.empty[String]

  override def readCommand(): String = {
    if (queue.isEmpty) {
      "exit"
    } else {
      queue.dequeue()
    }
  }

  def queueCommands(commands: String*): Unit = {
    queue.enqueue(commands :_*)
  }

  override def writeLine(line: String): Unit = {
    output = output :+ line
  }

  def getOutput: Vector[String] = output

  override def showCursor(): Unit = ()
}
