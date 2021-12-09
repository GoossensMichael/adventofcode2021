import java.util.stream.Collectors

fun main() {

    object : AoC(day = 8) {
        override fun part1(input: List<String>): Number {
            return input.stream()
                .map { it.split(" | ") }
                .map { it[1] }
                .map { it.split(' ').groupBy { i -> i.length } }
                .flatMap { it.entries.stream() }
                .filter { it.key == 2 || it.key == 4 || it.key == 3 || it.key == 7 }
                .mapToInt { it.value.size }
                .sum()
        }

        override fun check1ExpectedResult(): Number {
            return 26
        }

        private fun minus(numbers: List<String>, number: String): MutableMap<Int, MutableList<Pair<String, Int>>> {
            val collect = numbers.stream()
                .map {
                    Pair(it, it.toCharArray()
                        .filter { c -> !number.contains(c) }
                        .joinToString("")
                        .length)
                }
                .collect(Collectors.groupingBy({ it.second }, Collectors.toList()))

            return collect
        }

        private fun solve2(input: Pair<Map<Int, List<String>>, List<String>>): Int {
            val codes = input.first
            val numbers = mutableMapOf<String, String>()

            val one = codes[2]!![0]
            val four = codes[4]!![0]
            val seven = codes[3]!![0]
            val eight = codes[7]!![0]

            val grp235 = codes[5]!!
            val three = minus(grp235, one)[3]!!.stream().findFirst().orElseThrow().first
            val two = minus(grp235, four)[3]!!.stream().findFirst().orElseThrow().first
            val five = minus(grp235, two)[2]!!.stream().findFirst().orElseThrow().first

            val grp069 = codes[6]!!
            val six = minus(grp069, one)[5]!!.stream().findFirst().orElseThrow().first
            val nine = minus(grp069, four)[2]!!.stream().findFirst().orElseThrow().first
            val zero = minus(grp069, five)[2]!!.stream().findFirst().orElseThrow().first

            numbers[zero] = "0"
            numbers[one] = "1"
            numbers[two] = "2"
            numbers[three] = "3"
            numbers[four] = "4"
            numbers[five] = "5"
            numbers[six] = "6"
            numbers[seven] = "7"
            numbers[eight] = "8"
            numbers[nine] = "9"

            val result = input.second.stream()
                .map { numbers[it] }
                .collect(Collectors.joining())

            return Integer.parseInt(result)
        }

        fun part2Pragmatic(input: List<String>): Number {
            return input.stream()
                .map { it.split(" | ") }
                .map { Pair(
                    it[0].split(' ').map { i -> i.toCharArray().sorted().joinToString("") }.groupBy { i -> i.length },
                    it[1].split(' ').map { i -> i.toCharArray().sorted().joinToString("") })
                }
                .mapToInt { solve2(it) }
                .sum()
        }

        private fun encode(wiring: String, segmentCount: Map<Char, Int>): String {
            return wiring.toCharArray()
                .map { c -> segmentCount[c] }
                .joinToString("")
                .toCharArray()
                .sorted()
                .joinToString("")
        }

        override fun part2(input: List<String>): Number {
            val referenceSolution = "abcefg cf acdeg acdfg bcdf abdfg abdefg acf abcdefg abcdfg"
            val segmentCount = referenceSolution.replace(" ", "").toList().groupingBy { it }.eachCount()

            val digitByCode = mutableMapOf<String, Int>()
            for ((index, wiring) in referenceSolution.split(' ').withIndex()) {
                digitByCode[encode(wiring, segmentCount)] = index
            }

            return input.stream()
                .mapToInt {
                    val split = it.split(" | ")
                    val wiring = split[0]
                    val output = split[1]

                    val inputSegmentCount = wiring.replace(" ", "").toList().groupingBy { i -> i }.eachCount()

                    var result = ""
                    for (digit in output.split(' ')) {
                        val code = encode(digit, inputSegmentCount)
                        result += digitByCode[code]
                    }

                    Integer.parseInt(result)
                }.sum()
        }

        override fun check2ExpectedResult(): Number {
            return 61229
        }

    }.execute()

}
