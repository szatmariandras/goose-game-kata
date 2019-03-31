package hu.andrasszatmari.goosegame

import atto._, Atto._
import cats.implicits._

object CommandParser {

  private val userName = many(letterOrDigit | oneOf("-_"))

  private val addPlayer: Parser[Command] =
    (stringCI("add player ") ~> userName <~ endOfInput).map(name => AddPlayer(name.mkString))

  private val movePlayer: Parser[Command] =
    (stringCI("move ") ~> userName <~ spaceChar, many(digit) <~ char(',') <~ spaceChar, many(digit) <~ endOfInput)
      .mapN((name, a, b) => MovePlayer(name.mkString, a.mkString.toInt, b.mkString.toInt))

  private val movePlayerRandom: Parser[Command] =
    (stringCI("move ") ~> userName <~ endOfInput).map(name => MovePlayerRandom(name.mkString))

  private val exit: Parser[Command] =
    stringCI("exit").map(_ => Exit)

  private val help: Parser[Command] =
    stringCI("help").map(_ => Help)

  private val parser: Parser[Command] =
      addPlayer |
      movePlayer |
      movePlayerRandom |
      exit |
      help

  def parse(input: String): Option[Command] = {
    parser.parseOnly(input) match {
      case ParseResult.Done(_, result) => Some(result)
      case _                           => None
    }
  }

}
