package hu.andrasszatmari.goosegame

import org.scalatest.{ FlatSpec, Matchers }

class CommandParserTest extends FlatSpec with Matchers {

  behavior of "CommandParser.parse"

  it should "parse add player commands correctly" in {
    CommandParser.parse("add player Béla") shouldBe Some(AddPlayer("Béla"))
    CommandParser.parse("add player Béla_Kettő") shouldBe Some(AddPlayer("Béla_Kettő"))
    CommandParser.parse("add player Béla Kettő") shouldBe None
  }

  it should "parse move player random commands correctly" in {
    CommandParser.parse("move Béla") shouldBe Some(MovePlayerRandom("Béla"))
    CommandParser.parse("move Béla_Kettő") shouldBe Some(MovePlayerRandom("Béla_Kettő"))
    CommandParser.parse("move Béla Kettő") shouldBe None
  }

  it should "parse move player commands correctly" in {
    CommandParser.parse("move Béla 1, 2") shouldBe Some(MovePlayer("Béla", 1, 2))
    CommandParser.parse("move Béla_Kettő 3, 4") shouldBe Some(MovePlayer("Béla_Kettő", 3, 4))
    CommandParser.parse("move Béla Kettő 3, 4") shouldBe None
  }

  it should "parse exit command correctly" in {
    CommandParser.parse("exit") shouldBe Some(Exit)
  }

}
