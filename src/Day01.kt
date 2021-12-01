fun main() {

    val day = 1;

    fun part1(input: List<String>): Int {
        var previous : Int? = null
        var count = 0
        for(depth in input.stream().mapToInt(Integer::parseInt)) {
            if (previous != null) {
                if (previous < depth) {
                    count++
                }
            }
            previous = depth
        }

        return count
    }

    fun part2(input: List<String>): Int {
        val depths = input.stream().mapToInt(Integer::parseInt).toArray()
        var count = 0
        for(i in 0..depths.size-4) {
            if (depths[i] + depths[i+1] + depths[i+2] < depths[i+1] + depths[i+2] + depths[i+3]) {
                count++
            }
        }

        return count
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
