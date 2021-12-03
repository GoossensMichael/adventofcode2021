import java.util.stream.Collectors

fun main() {

    val day = 3;

    fun part1(input: List<String>): Int {

        var gammaBits = ""
        var epsilonBits = ""

        for (i in 0 until input.get(0).length) {
            val bitValueCount = input.stream()
                .map({ it.get(i) })
                .collect(Collectors.groupingBy({ it }, Collectors.counting()))

            val oneBits = bitValueCount.getOrDefault('1', 0)
            val zeroBits = bitValueCount.getOrDefault('0', 0)

            val gamma = if (oneBits > zeroBits) '1' else '0'
            val epsilon = if (oneBits < zeroBits) '1' else '0'

            gammaBits += gamma
            epsilonBits += epsilon
        }

        return Integer.parseInt(gammaBits, 2) * Integer.parseInt(epsilonBits, 2)
    }

    tailrec fun solve(input: List<String>, position: Int, type: String): String {
        if (input.size < 2) {
            return input.get(0)
        }

        val high = mutableListOf<String>();
        val low = mutableListOf<String>();

        for(i in input) {
            if (i.get(position) == '1') {
                high.add(i)
            } else {
                low.add(i)
            }
        }

        val check = if (type == "mostCommon") high.size >= low.size else high.size < low.size

        return if (check) solve(high, position + 1, type) else solve(low, position + 1, type)
    }

    fun part2(input: List<String>): Int {
        val oxygen = solve(input, 0, "mostCommon")
        val co2 = solve(input, 0, "leastCommon")

        println("oxy $oxygen co2 $co2")
        println("oxy ${Integer.parseInt(oxygen, 2)} co2 ${Integer.parseInt(co2, 2)}")
        return Integer.parseInt(oxygen, 2) * Integer.parseInt(co2, 2)
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
    check(part2(testInput) == 230)

    // get the answer with the real data for part 2
    treatPart(2, part2(input))
}
