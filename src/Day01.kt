fun main() {

    val day = 1

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

    // test if implementation meets criteria from the description for part 1, like:
    val testInput = readInput(String.format("Day%02d_test", day))
    check(part1(testInput) == 7)

    // test was ok retrieve the real data
    val input = readInput(String.format("Day%02d", day))

    // get the answer with the real data for part 1
    treatPart(1, part1(input), day)

    // test if implementation meets criteria from the description for part 2, like:
    check(part2(testInput) == 5)

    // get the answer with the real data for part 2
    treatPart(2, part2(input), day)
}
