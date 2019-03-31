package hu.andrasszatmari.goosegame

import hu.andrasszatmari.goosegame.lib._

class GameTest extends BoardTest {

  behavior of "Adding players to the game"

  it should "allow players to be added" in executeOnBoard {
    givenThereIsNoParticipant()
    whenTheUserWrites("add player Pippo")
    thenTheSystemResponds("players: Pippo")
    whenTheUserWrites("add player Pluto")
    thenTheSystemResponds("players: Pippo, Pluto")
  }

  it should "no allow duplicated players to be added" in executeOnBoard {
    givenThereAreParticipants("Pippo")
    whenTheUserWrites("add player Pippo")
    thenTheSystemResponds("Player Pippo is already on the board.")
  }

  behavior of "Moving players"

  it should "should be working on the board so progress is made" in executeOnBoard {
    givenThereAreParticipantsOnSpaces("Pippo" -> 0, "Pluto" -> 0)
    whenTheUserWrites("move Pippo 4, 3")
    thenTheSystemResponds("Pippo rolls 4, 3. Pippo moves from Start to 7")
    whenTheUserWrites("move Pluto 2, 2")
    thenTheSystemResponds("Pluto rolls 2, 2. Pluto moves from Start to 4")
    whenTheUserWrites("move Pippo 2, 3")
    thenTheSystemResponds("Pippo rolls 2, 3. Pippo moves from 7 to 12")
  }

  behavior of "Winning"

  it should "happen when a player lands on space 63" in executeOnBoard {
    givenThereAreParticipantsOnSpaces("Pippo" -> 60)
    whenTheUserWrites("move Pippo 1, 2")
    thenTheSystemResponds("Pippo rolls 1, 2. Pippo moves from 60 to 63. Pippo Wins!!")
  }

  it should "only happen with the exact dice shooting" in executeOnBoard {
    givenThereAreParticipantsOnSpaces("Pippo" -> 60)
    whenTheUserWrites("move Pippo 3, 2")
    thenTheSystemResponds("Pippo rolls 3, 2. Pippo moves from 60 to 63. Pippo bounces! Pippo returns to 61")
  }

  behavior of "Dice"

  it should "be thrown by the game if not specified" in executeOnBoard {
    givenThereAreParticipantsOnSpaces("Pippo" -> 4)
    givenTheDiceWillRoll(1, 2)
    whenTheUserWrites("move Pippo")
    thenTheSystemResponds("Pippo rolls 1, 2. Pippo moves from 4 to 7")
  }

  behavior of "The Bridge"

  it should "jump players from space 6 to 12" in executeOnBoard {
    givenThereAreParticipantsOnSpaces("Pippo" -> 4)
    givenTheDiceWillRoll(1, 1)
    whenTheUserWrites("move Pippo")
    thenTheSystemResponds("Pippo rolls 1, 1. Pippo moves from 4 to The Bridge. Pippo jumps to 12")
  }

  behavior of "The Goose"

  it should "jump players with the same amount as they rolled before - single jump" in executeOnBoard {
    givenThereAreParticipantsOnSpaces("Pippo" -> 3)
    givenTheDiceWillRoll(1, 1)
    whenTheUserWrites("move Pippo")
    thenTheSystemResponds("Pippo rolls 1, 1. Pippo moves from 3 to 5, The Goose. Pippo moves again and goes to 7")
  }

  it should "jump players with the same amount as they rolled before - double jump" in executeOnBoard {
    givenThereAreParticipantsOnSpaces("Pippo" -> 10)
    givenTheDiceWillRoll(2, 2)
    whenTheUserWrites("move Pippo")
    thenTheSystemResponds("Pippo rolls 2, 2. Pippo moves from 10 to 14, The Goose. Pippo moves again and goes to 18, The Goose. Pippo moves again and goes to 22")
  }

  it should "jump players with the same amount as they rolled before - mega combo jump" in executeOnBoard {
    givenThereAreParticipantsOnSpaces("Pippo" -> 0)
    givenTheDiceWillRoll(6, 3)
    whenTheUserWrites("move Pippo")
    thenTheSystemResponds("Pippo rolls 6, 3. Pippo moves from Start to 9, The Goose. Pippo moves again and goes to 18, The Goose. Pippo moves again and goes to 27, The Goose. Pippo moves again and goes to 36")
  }

  behavior of "Prank"

  it should "send back player standing on the space I landed to the space I started from" in executeOnBoard {
    givenThereAreParticipantsOnSpaces("Pippo" -> 15, "Pluto" -> 17)
    givenTheDiceWillRoll(1, 1)
    whenTheUserWrites("move Pippo")
    thenTheSystemResponds("Pippo rolls 1, 1. Pippo moves from 15 to 17. On 17 there is Pluto, who returns to 15")
  }

  it should "result in some back and forth given the right circumstances" in executeOnBoard {
    givenThereAreParticipants("Pippo", "Pluto")
    givenTheDiceWillRoll((2, 3), (6, 4), (1, 4))
    whenTheUserWrites("move Pippo")
    thenTheSystemResponds("Pippo rolls 2, 3. Pippo moves from Start to 5, The Goose. Pippo moves again and goes to 10")
    whenTheUserWrites("move Pluto")
    thenTheSystemResponds("Pluto rolls 6, 4. Pluto moves from Start to 10. On 10 there is Pippo, who returns to Start")
    whenTheUserWrites("move Pippo")
    thenTheSystemResponds("Pippo rolls 1, 4. Pippo moves from Start to 5, The Goose. Pippo moves again and goes to 10. On 10 there is Pluto, who returns to 5")
  }

  it should "work over multiple Geese" in executeOnBoard {
    givenThereAreParticipants("Pippo", "Pluto", "Paperino")
    givenTheDiceWillRoll((3, 6), (3, 6), (3, 6))
    whenTheUserWrites("move Pippo")
    thenTheSystemResponds("Pippo rolls 3, 6. Pippo moves from Start to 9, The Goose. Pippo moves again and goes to 18, The Goose. Pippo moves again and goes to 27, The Goose. Pippo moves again and goes to 36")
    whenTheUserWrites("move Pluto")
    thenTheSystemResponds("Pluto rolls 3, 6. Pluto moves from Start to 9, The Goose. Pluto moves again and goes to 18, The Goose. Pluto moves again and goes to 27, The Goose. Pluto moves again and goes to 36. On 36 there is Pippo, who returns to 27")
    whenTheUserWrites("move Paperino")
    thenTheSystemResponds("Paperino rolls 3, 6. Paperino moves from Start to 9, The Goose. Paperino moves again and goes to 18, The Goose. Paperino moves again and goes to 27, The Goose. On 27 there is Pippo, who returns to 18. Paperino moves again and goes to 36. On 36 there is Pluto, who returns to 27")
  }

  it should "not move players forward, but can move multiple players backward" in executeOnBoard {
    givenThereAreParticipantsOnSpaces("Pluto" -> 62, "Paperino" -> 61, "Pippo" -> 53)
    givenTheDiceWillRoll((4, 1), (5, 1), (4 ,2))
    whenTheUserWrites("move Pluto")
    thenTheSystemResponds("Pluto rolls 4, 1. Pluto moves from 62 to 63. Pluto bounces! Pluto returns to 59")
    whenTheUserWrites("move Paperino")
    thenTheSystemResponds("Paperino rolls 5, 1. Paperino moves from 61 to 63. Paperino bounces! Paperino returns to 59")
    whenTheUserWrites("move Pippo")
    thenTheSystemResponds("Pippo rolls 4, 2. Pippo moves from 53 to 59. On 59 there are Pluto, Paperino, who return to 53")
  }

}
