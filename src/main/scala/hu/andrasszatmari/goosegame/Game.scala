package hu.andrasszatmari.goosegame

import cats.data.Validated
import cats.implicits._

import scala.annotation.tailrec

class Game(io: IO, dice: Dice) {
  import Game._

  def run(board: Board): Board = {
    next(board, ReadNext)
  }

  @tailrec
  private def next(board: Board, command: Command): Board = {
    Validation.validateCommand(board, command) match {
      case Validated.Invalid(errors) => next(board, InvalidArguments(errors))
      case Validated.Valid(_) =>
        command match {
          case Exit =>
            board
          case Help =>
            displayHelp()

            next(board, ReadNext)
          case InvalidCommand =>
            io.writeLine("Sorry, I couldn't recognize this command. Please try again.")

            next(board, ReadNext)
          case InvalidArguments(errors) =>
            errors.map(io.writeLine)

            next(board, ReadNext)
          case ReadNext =>
            io.showCursor()
            val nextCmd = CommandParser.parse(io.readCommand()).getOrElse(InvalidCommand)

            next(board, nextCmd)
          case AddPlayer(name) =>
            val newBoard = board.copy(players = board.players + name)

            io.writeLine(s"players: ${newBoard.players.mkString(", ")}")

            next(newBoard, ReadNext)
          case MovePlayer(player, roll1, roll2) =>
            val (newBoard, log) = step(board, player, roll1 + roll2)
            val nextCmd = if (gameOver(newBoard)) Exit else ReadNext

            io.writeLine((s"${player} rolls ${roll1}, ${roll2}" :: log).mkString(". "))

            next(newBoard, nextCmd)
          case MovePlayerRandom(player) =>
            next(board, MovePlayer(player, dice.roll(), dice.roll()))
        }
    }
  }

  private def step(board: Board, player: String, steps: Int): (Board, List[String]) = {
    val prevPos = board.positions.getOrElse(player, 0)
    val nextPos = prevPos + steps

    if (nextPos == FINAL_SPACE) {
      (
        updatePlayerPosition(board, player, FINAL_SPACE),
        List(s"${player} moves from ${Position(prevPos)} to 63. ${player} Wins!!")
      )
    } else if (nextPos > 63) {
      val finalPos = 63 - Math.abs(63 - nextPos)
      movePlayer(
        board,
        player,
        prevPos,
        finalPos,
        List(
          s"${player} moves from ${Position(prevPos)} to 63",
          s"${player} bounces! ${player} returns to ${Position(finalPos)}"
        )
      )
    } else if (nextPos == THE_BRIDGE) {
      movePlayer(
        board,
        player,
        prevPos,
        THE_BRIDGE_TARGET,
        List(
          s"${player} moves from ${Position(prevPos)} to ${Position(nextPos)}",
          s"${player} jumps to ${Position(THE_BRIDGE_TARGET)}"
        )
      )
    } else if (THE_GEESE.contains(nextPos)) {
      handleGooseStep(board, player, prevPos, nextPos, steps, false, List.empty)
    } else {
      movePlayer(
        board,
        player,
        prevPos,
        nextPos,
        List(s"${player} moves from ${Position(prevPos)} to ${Position(nextPos)}")
      )
    }
  }

  private def gameOver(board: Board): Boolean = board.positions.exists(_._2 == FINAL_SPACE)

  @tailrec
  private def handleGooseStep(
    board: Board, player: String, prevPos: Int, nextPos: Int, steps: Int, again: Boolean, log: List[String]
  ): (Board, List[String]) = {
    val middle = if (again) "again and goes" else s"from ${Position(prevPos)}"
    val nextIsGoose = THE_GEESE.contains(nextPos)
    val (newBoard, newLog) = movePlayer(
      board,
      player,
      prevPos,
      nextPos,
      log ++ List(s"${player} moves ${middle} to ${Position(nextPos).withGoose}")
    )
    if (nextIsGoose) {
      handleGooseStep(newBoard, player, nextPos, nextPos + steps, steps, true, newLog)
    } else {
      (newBoard, newLog)
    }
  }

  private def handlePranks(
    board: Board, player: String, prevPos: Int, currPos: Int, log: List[String]
  ): (Board, List[String]) = {
    val (newBoard, happenings) = if (prevPos >= currPos) {
      // No pranks when the landed at the same spot it started from or lower because of a bounce back from 63,
      // so, e.g., don't "prank" people from 59 to 61
      (board, List.empty)
    } else {
      val others = board.positions.filter { case (pl, pos) => pos == currPos && pl != player }.keys.toList
      others match {
        case Nil           => (board, List.empty)
        case oneGuy :: Nil =>
          (
            updatePlayerPosition(board, oneGuy, prevPos),
            List(s"On ${Position(currPos)} there is ${oneGuy}, who returns to ${Position(prevPos)}")
          )
        case moreGuys      =>
          val newBoard = moreGuys.foldLeft(board) { case (board, oneGuy) => updatePlayerPosition(board, oneGuy, prevPos)}
          (
            newBoard,
            List(s"On ${Position(currPos)} there are ${moreGuys.mkString(", ")}, who return to ${Position(prevPos)}")
          )
      }
    }

    (newBoard, log ++ happenings)
  }

  private def movePlayer(
    board: Board, player: String, prevPos: Int, nextPos: Int, log: List[String]
  ): (Board, List[String]) = {
    handlePranks(
      updatePlayerPosition(board, player, nextPos),
      player,
      prevPos,
      nextPos,
      log
    )
  }

  private def updatePlayerPosition(board: Board, player: String, nextPos: Int): Board = {
    board.copy(positions = board.positions.updated(player, nextPos))
  }

  private def displayHelp(): Unit = {
    io.writeLine("Available commands:")
    io.writeLine("")
    io.writeLine("exit                            Exit application")
    io.writeLine("help                            Display this help message")
    io.writeLine("add player <player>             Add player to the game")
    io.writeLine("move <player>                   Move player by rolling the dice")
    io.writeLine("move <player> <roll1> <roll2>   Move player with dice rolls specified")
  }
}

object Game {
  private val FINAL_SPACE = 63
  private val THE_BRIDGE = 6
  private val THE_BRIDGE_TARGET = 12
  private val THE_GEESE = Set(5, 9, 14, 18, 23, 27)

  final case class Board(players: Set[String], positions: Map[String, Int])

  object Board {
    val empty: Board = Board(Set.empty[String], Map.empty[String, Int])
  }

  final case class Position(position: Int) {
    override def toString: String = {
      if (position == 0) "Start"
      else if (position == 6) "The Bridge"
      else position.toString
    }

    def withGoose: String = toString + (if (THE_GEESE.contains(position)) ", The Goose" else "")
  }
}
