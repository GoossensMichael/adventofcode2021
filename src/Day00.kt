fun main() {

    val day = 0

    fun part1(input: List<String>): Number {
        return 0
    }

    fun part2(input: List<String>): Number {
        return 0
    }

    // test if implementation meets criteria from the description for part 1, like:
    val testInput = readInput(String.format("Day%02d_test", day))
    check(part1(testInput) == 0)

    // test was ok retrieve the real data
    val input = readInput(String.format("Day%02d", day))

    // get the answer with the real data for part 1
    treatPart(1, part1(input), day)

    // test if implementation meets criteria from the description for part 2, like:
    check(part2(testInput) == 0)

    // get the answer with the real data for part 2
    treatPart(2, part2(input), day)
}
