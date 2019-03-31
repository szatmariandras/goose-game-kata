package hu.andrasszatmari.goosegame.lib

import hu.andrasszatmari.goosegame.Game
import hu.andrasszatmari.goosegame.Game.Board
import hu.andrasszatmari.goosegame.lib._
import org.scalatest.{ BeforeAndAfterEach, FlatSpec, Matchers, Outcome, Retries }

import scala.collection.mutable

abstract class BoardTest extends FlatSpec with Matchers with Retries {

  private var io: MockIO = _
  private var dice: MockDice = _
  private var expectedIO: mutable.Queue[String] = _
  private var players: Set[String] = _
  private var positions: Map[String, Int] = _

  protected def givenThereIsNoParticipant(): Unit = {
    players = Set.empty[String]
    positions = Map.empty[String, Int]
  }

  protected def givenThereAreParticipantsOnSpaces(playersToSpaces: (String, Int)*): Unit = {
    players = playersToSpaces.map(_._1).toSet
    positions = playersToSpaces.toMap
  }

  protected def givenThereAreParticipants(newPlayers: String*): Unit = {
    players = newPlayers.toSet
  }

  protected def givenTheDiceWillRoll(a: Int, b: Int): Unit = {
    dice.enqueue(a, b)
  }

  protected def givenTheDiceWillRoll(a: (Int, Int)*): Unit = {
    a.foreach(rolls => dice.enqueue(rolls._1, rolls._2))
  }

  protected def whenTheUserWrites(input: String): Unit = {
    io.queueCommands(input)
  }

  protected def thenTheSystemResponds(output: String): Unit = {
    expectedIO.enqueue(output)
  }

  protected def executeOnBoard(testCode: => Any): Unit = {
    io = new MockIO()
    dice = new MockDice
    expectedIO = new mutable.Queue[String]
    players = Set.empty[String]
    positions = Map.empty[String, Int]

    testCode

    val board = Board(players, positions)
    val game = new Game(io, dice)
    val _ = game.run(board)
    io.getOutput shouldBe expectedIO.toVector
  }

}
