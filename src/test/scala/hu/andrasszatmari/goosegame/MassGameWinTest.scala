package hu.andrasszatmari.goosegame

import hu.andrasszatmari.goosegame.Game.Board
import hu.andrasszatmari.goosegame.lib._

import org.scalatest._
import org.scalatest.tagobjects.{ Retryable, Slow }

class MassGameWinTest extends FlatSpec with Matchers with Retries {

  behavior of "Game"

  it should "always end with a win" taggedAs (Slow, Retryable) in {
    /**
      * Run 10.000 random games.
      * This might fail if there's an extreme amount of bouncing at the end,
      * however it hasn't happened even after running millions of simulations.
      * Still, marked as [[Retryable]] just to be on the safe side
      */

    for (_ <- 1 to 10000) {
      val commands = List(
        "help",
        "add player Pippo",
        "add player Pluto",
        "add player Paperino"
      ) ++ List.fill(1000)(List("move Pippo", "move Pluto", "move Paperino")).flatten ++ List("exit")
      val io = new MockIO(commands)
      val dice = new MockDice
      val game = new Game(io, dice)
      val board = game.run(Board.empty)

      if (!io.getOutput.last.endsWith("Wins!!")) {
        fail(s"Game hasn't ended with a win: ${board} \n" + io.getOutput.mkString("\n"))
      }

      succeed
    }
  }

  override def withFixture(test: NoArgTest): Outcome = {
    if (isRetryable(test))
      withRetry { super.withFixture(test) }
    else
      super.withFixture(test)
  }
}
