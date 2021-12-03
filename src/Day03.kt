import java.util.stream.Collectors

fun main() {

    val day = 3;

    fun part1(input: List<String>): Int {

        var gammaBits = ""
        var epsilonBits = ""

        for (i in 0..input.get(0).length-1) {
            val bitValueCount = input.stream()
                .map({ it.get(i) })
                .collect(Collectors.groupingBy({ it }, Collectors.counting()))

            val gamma =
                if ((bitValueCount.get('1') ?: 0) > (bitValueCount.get('0') ?: 0)) {
                    '1'
                } else {
                    '0'
                }

            val epsilon =
                if ((bitValueCount.get('1') ?: 0) < (bitValueCount.get('0') ?: 0)) {
                    '1'
                } else {
                    '0'
                }

            gammaBits += gamma
            epsilonBits += epsilon
        }

        return Integer.parseInt(gammaBits, 2) * Integer.parseInt(epsilonBits, 2)
    }

    fun part2(input: List<String>): Int {
        return 0
    }

    fun treatPart(part: Int, answer: Int) {
        print("Submit part $part result $answer? (y|n): ")
        if (readLine() == "y") {
            sendAnswer(day, part, answer)
        }
    }

    // test if implementation meets criteria from the description for part 1, like:
    val testInput = readInput(String.format("Day%02d_test", day))
    check(part1(testInput) == 198)

    // test was ok retrieve the real data
    val input = readInput(String.format("Day%02d", day))

    // get the answer with the real data for part 1
    treatPart(1, part1(input))

    // test if implementation meets criteria from the description for part 2, like:
    check(part2(testInput) == 0)

    // get the answer with the real data for part 2
    treatPart(2, part2(input))
}
