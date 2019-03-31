package hu.andrasszatmari.goosegame

import hu.andrasszatmari.goosegame.Game.Board

object Main extends App {

  println("Welcome to the Goose Game. Type `help` to show available commands.")

  val game = new Game(new StdInIO, new RandomDice)
  game.run(Board.empty)

}
