package hu.andrasszatmari.goosegame.lib

import hu.andrasszatmari.goosegame.Dice

import scala.collection.mutable

class MockDice extends Dice {
  private val queue = mutable.Queue.empty[Int]

  def roll(): Int = {
    if (queue.isEmpty) {
      1 + scala.util.Random.nextInt(6)
    } else {
      queue.dequeue()
    }
  }

  def enqueue(rolls: Int*): Unit = queue.enqueue(rolls: _*)
}

