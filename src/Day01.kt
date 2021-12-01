fun main() {

    val day = 1;

    fun solve(input: List<String>, window: Int): Int {
        val depths = input.stream().mapToInt(Integer::parseInt).toArray()
        var count = 0
        for(i in 0..depths.size-(window + 1)) {
            if (depths[i] < depths[i+window]) {
                count++
            }
        }

        return count
    }

    fun part1(input: List<String>): Int {
        return solve(input, 1)
    }

    fun part2(input: List<String>): Int {
        return solve(input, 3)
    }

    fun treatPart(part: Int, answer: Int) {
        print("Submit part $part result $answer? (y|n): ")
        if (readLine() == "y") {
            sendAnswer(day, part, answer)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(String.format("Day%02d_test", day))
    check(part1(testInput) == 7)

    val input = readInput(String.format("Day%02d", day))
    treatPart(1, part1(input))

    check(part2(testInput) == 5)
    treatPart(2, part2(input))
}
