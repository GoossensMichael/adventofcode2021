import kotlin.math.abs

fun main() {

    object : AoC(day = 7) {
        override fun part1(input: List<String>): Number {
            val sorted = input[0].split(',')
                .map{ Integer.parseInt(it) }
                .toIntArray()
                .sorted()

            val meetingPoint =
                if (sorted.size % 2 == 0) {
                    (sorted[sorted.size / 2] + sorted[sorted.size / 2 - 1]) / 2
                } else {
                    sorted[sorted.size / 2]
                }


            return sorted.asSequence()
                .map { abs(it - meetingPoint) }
                .sum()
        }

        override fun check1ExpectedResult(): Number {
            return 37
        }

        override fun part2(input: List<String>): Number {
            val positions = input[0].split(',').map { Integer.parseInt(it) }.toIntArray()
            val min = positions.minOrNull() ?: 0
            val max = positions.maxOrNull() ?: 0

            var answer = Int.MAX_VALUE

            for (otherPosition in min..max) {
                var newAnswer = 0
                for (position in positions) {
                    val distance = abs(otherPosition - position)
                    val fuelCost = (distance * (distance + 1)) / 2
                    newAnswer += fuelCost
                }

                if (newAnswer < answer) {
                    answer = newAnswer
                }
            }

            return answer
        }

        override fun check2ExpectedResult(): Number {
            return 168
        }

    }.execute()

}
