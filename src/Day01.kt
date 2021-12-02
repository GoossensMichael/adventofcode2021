fun main() {

    val day = 1;
    val dayWithPadding: String = day.toString().padStart(2, '0')

    fun solve(input: List<String>, window: Int): Int {
        // To use the stream mapping solution from kotlin, you could use: 
        // val depths = input.stream().mapToInt(Integer::parseInt).toArray()
        val depths = input.asSequence().map { it.toInt()}.toList()
        // You could just map it directly as well, given it's just a step
        val depths1 = input.map { it.toInt() }
        
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

    // test if implementation meets criteria from the description for part 1, like:
    // You can build strings directly using interpolation, even when padding the number. Not the best, but maybe it could be written before?
    // val testInput = readInput(String.format("Day%02d_test", day))
    val testInput = readInput("Day$dayWithPadding_test")
    
    check(part1(testInput) == 7)

    // test was ok retrieve the real data
    val input = readInput(String.format("Day$dayWithPadding", day))

    // get the answer with the real data for part 1
    treatPart(1, part1(input))

    // test if implementation meets criteria from the description for part 2, like:
    check(part2(testInput) == 5)

    // get the answer with the real data for part 2
    treatPart(2, part2(input))
}
