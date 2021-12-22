import kotlin.math.max

fun main() {

    data class Player(val position: Long, val score: Long)
    data class DiracDiceUniverse(val playerOne: Player, val playerTwo: Player, val turn: Int)

    class DeterministicDie(val sides: Long) {
        var amountOfRolls = 0
        var currentValue = 0L

        fun roll(): Long {
            currentValue = if (currentValue == sides) 1 else currentValue + 1
            amountOfRolls++
            return currentValue
        }

    }

    val quantumDiePermutations = listOf(
        3L, 4L, 5L,
        4L, 5L, 6L,
        5L, 6L, 7L,
        4L, 5L, 6L,
        5L, 6L, 7L,
        6L, 7L, 8L,
        5L, 6L, 7L,
        6L, 7L, 8L,
        7L, 8L, 9L,
    )

    object : AoC(day = 21) {

        override fun part1(input: List<String>): Number {
            return play(input)
        }

        override fun check1ExpectedResult(): Number {
            return 739785L
        }

        override fun part2(input: List<String>): Number {
            val playerPositions = input.parse()


            val startUniverse = DiracDiceUniverse(
                Player(playerPositions.first, 0L),
                Player(playerPositions.second, 0L), 1
            )
            val universes = mutableMapOf<DiracDiceUniverse, Pair<Long, Long>>()

            val r = play2(startUniverse, universes)

            return max(r.first, r.second)
        }

        override fun check2ExpectedResult(): Number {
            return 444356092776315L
        }

        private fun play2(
            universe: DiracDiceUniverse,
            universes: MutableMap<DiracDiceUniverse, Pair<Long, Long>>
        ): Pair<Long, Long> {
            fun evaluatePlayer(player: Player, steps: Long): Player {
                val position = determinePawnPosition(player.position, steps)

                return Player(position, player.score + position)
            }

            if (universes.contains(universe)) {
                return universes[universe]!!
            }

            if (universe.playerOne.score >= 21) {
                universes[universe] = Pair(1, 0)
                return universes[universe]!!
            } else if (universe.playerTwo.score >= 21) {
                universes[universe] = Pair(0, 1)
                return universes[universe]!!
            }

            var state = Pair(0L, 0L)
            for (i in quantumDiePermutations) {
                val childState = play2(
                    DiracDiceUniverse(
                        if (universe.turn == 1) evaluatePlayer(universe.playerOne, i) else universe.playerOne,
                        if (universe.turn == 2) evaluatePlayer(universe.playerTwo, i) else universe.playerTwo,
                        if (universe.turn == 1) 2 else 1
                    ),
                    universes
                )
                state = Pair(state.first + childState.first, state.second + childState.second)
            }
            universes[universe] = state

            return state
        }

        private fun play(input: List<String>): Number {
            val die = DeterministicDie(100)
            val playerPositions = input.parse()

            val players = arrayOf(Pair(playerPositions.first, 0L), Pair(playerPositions.second, 0L))

            var noWinner = true
            while (noWinner) {
                var i = 0
                while (i < players.size && noWinner) {
                    val player = players[i]
                    val rollScore = die.roll() + die.roll() + die.roll()
                    val playerPosition = determinePawnPosition(player.first, rollScore)
                    val playerScore = player.second + playerPosition
                    players[i] = Pair(playerPosition, playerScore)

                    if (playerScore >= 1000) {
                        noWinner = false
                    }

                    i++
                }
            }

            return players.first { it.second < 1000 }.second * die.amountOfRolls
        }

        private fun determinePawnPosition(position: Long, steps: Long): Long {
            val addition = position + steps
            val newPosition =
                if (addition > 10) {
                    if (addition % 10 == 0L) 10 else addition % 10
                } else {
                    addition
                }

            return newPosition
        }

        private fun List<String>.parse(): Pair<Long, Long> {
            val playerInfo = "Player x starting position: "
            return Pair(
                this[0].substring(playerInfo.length).toLong(),
                this[1].substring(playerInfo.length).toLong()
            )
        }

    }.execute()

}
