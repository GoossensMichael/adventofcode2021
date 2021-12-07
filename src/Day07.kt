fun main() {

    val day = 7

    fun part1(input: List<String>): Int {
        val sorted = input.get(0).split(',')
            .map{ Integer.parseInt(it) }
            .toIntArray()
            .sorted()

        val meetingPoint =
        if (sorted.size % 2 == 0) {
            (sorted[sorted.size / 2] + sorted[sorted.size / 2 - 1]) / 2
        } else {
            sorted[sorted.size / 2]
        }


        val sum = sorted.stream()
            .mapToInt { it }
            .map { Math.abs(it - meetingPoint) }
            .sum()

        return sum
    }

    fun part2(input: List<String>): Int {
        val positions = input.get(0).split(',').map { Integer.parseInt(it) }.toIntArray()
        val min = positions.minOrNull() ?: 0
        val max = positions.maxOrNull() ?: 0

        var answer = Int.MAX_VALUE

        for (otherPosition in min..max) {
            var newAnswer = 0
            for (position in positions) {
                val distance = Math.abs(otherPosition - position)
                val fuelCost = (distance * (distance + 1)) / 2
                newAnswer += fuelCost
            }

            if (newAnswer < answer) {
                answer = newAnswer
            }
        }

        return answer
    }

    fun treatPart(part: Int, answer: Int) {
        print("Submit part $part result $answer? (y|n): ")
        if (readLine() == "y") {
            sendAnswer(day, part, answer)
        }
    }

    // test if implementation meets criteria from the description for part 1, like:
    val testInput = readInput(String.format("Day%02d_test", day))
    check(part1(testInput) == 37)

    // test was ok retrieve the real data
    val input = readInput(String.format("Day%02d", day))

    // get the answer with the real data for part 1
    treatPart(1, part1(input))

    // test if implementation meets criteria from the description for part 2, like:
    check(part2(testInput) == 168)

    // get the answer with the real data for part 2
    treatPart(2, part2(input))
}
