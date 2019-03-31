package hu.andrasszatmari.goosegame

import org.scalatest.{ FlatSpec, Matchers }

class CommandParserTest extends FlatSpec with Matchers {

  behavior of "CommandParser.parse"

  it should "parse add player commands correctly" in {
    CommandParser.parse("add player Pippo") shouldBe Some(AddPlayer("Pippo"))
    CommandParser.parse("add player Pippo_Pippo") shouldBe Some(AddPlayer("Pippo_Pippo"))
    CommandParser.parse("add player Pippo With Spaces") shouldBe None
  }

  it should "parse move player random commands correctly" in {
    CommandParser.parse("move Pippo") shouldBe Some(MovePlayerRandom("Pippo"))
    CommandParser.parse("move Pippo_Pippo") shouldBe Some(MovePlayerRandom("Pippo_Pippo"))
    CommandParser.parse("move Pippo With Spaces") shouldBe None
  }

  it should "parse move player commands correctly" in {
    CommandParser.parse("move Pippo 1, 2") shouldBe Some(MovePlayer("Pippo", 1, 2))
    CommandParser.parse("move Pippo_Pippo 3, 4") shouldBe Some(MovePlayer("Pippo_Pippo", 3, 4))
    CommandParser.parse("move Pippo With Spaces 3, 4") shouldBe None
  }

  it should "parse exit command correctly" in {
    CommandParser.parse("exit") shouldBe Some(Exit)
  }

  it should "parse help command correctly" in {
    CommandParser.parse("help") shouldBe Some(Help)
  }

}
