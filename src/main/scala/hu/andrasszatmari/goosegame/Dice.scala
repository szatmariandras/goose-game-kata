package hu.andrasszatmari.goosegame

abstract class Dice {
  def roll(): Int
}

class RandomDice extends Dice {
  def roll(): Int = 1 + scala.util.Random.nextInt(6)
}