package hu.andrasszatmari.goosegame

import hu.andrasszatmari.goosegame.Game.Board

import cats.implicits._
import cats.data.ValidatedNec

object Validation {
  type ValidationResult = ValidatedNec[String, Unit]

  private lazy val valid = ().validNec

  def validateCommand(board: Board, command: Command): ValidationResult = {
    command match {
      case c @ AddPlayer(_)        => validateAddPlayer(board, c)
      case c @ MovePlayer(_, _, _) => validateMovePlayer(board, c)
      case c @ MovePlayerRandom(_) => validateMovePlayerRandom(board, c)
      case _ => valid
    }
  }

  private def validateAddPlayer(board: Board, command: AddPlayer): ValidationResult = {
    validatePlayerAvailable(board, command.name)
  }

  private def validateMovePlayer(board: Board, command: MovePlayer): ValidationResult = {
    validatePlayerExists(board, command.player) combine
      validateRoll(command.roll1, 1) combine
      validateRoll(command.roll2, 2)
  }

  private def validateMovePlayerRandom(board: Board, command: MovePlayerRandom): ValidationResult = {
    validatePlayerExists(board, command.player)
  }

  private def validateRoll(roll: Int, index: Int): ValidationResult =
    if (roll >= 0 && roll <=6)
      valid
    else
      s"Roll #${index}: ${roll} is invalid. Must be within the [1,6] interval.".invalidNec

  private def validatePlayerExists(board: Board, player: String): ValidationResult =
    if (board.players.contains(player))
      valid
    else
      s"Player ${player} isn't on the board. Choose one of ${board.players.mkString(", ")}, or add the player to the board".invalidNec

  private def validatePlayerAvailable(board: Board, player: String): ValidationResult =
    if (!board.players.contains(player))
      valid
    else
      s"Player ${player} is already on the board.".invalidNec
}
