package hu.andrasszatmari.goosegame

import cats.data.NonEmptyChain

sealed abstract class Command extends Product with Serializable
case object ReadNext extends Command
case object Exit extends Command
case object Help extends Command
case object InvalidCommand extends Command
final case class InvalidArguments(errors: NonEmptyChain[String]) extends Command
final case class AddPlayer(name: String) extends Command
final case class MovePlayer(player: String, roll1: Int, roll2: Int) extends Command
final case class MovePlayerRandom(player: String) extends Command
