import java.util.stream.Collectors

fun main() {

    object : AoC(day = 3) {
        override fun part1(input: List<String>): Number {
            var gammaBits = ""
            var epsilonBits = ""

            for (i in 0 until input[0].length) {
                val bitValueCount = input.stream()
                    .map { it[i] }
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

        override fun check1ExpectedResult(): Number {
            return 198
        }

        private tailrec fun solve(input: List<String>, position: Int, type: String): String {
            if (input.size < 2) {
                return input[0]
            }

            val high = mutableListOf<String>()
            val low = mutableListOf<String>()

            for(i in input) {
                if (i[position] == '1') {
                    high.add(i)
                } else {
                    low.add(i)
                }
            }

            val check = if (type == "mostCommon") high.size >= low.size else high.size < low.size

            return if (check) solve(high, position + 1, type) else solve(low, position + 1, type)
        }

        override fun part2(input: List<String>): Number {
            val oxygen = solve(input, 0, "mostCommon")
            val co2 = solve(input, 0, "leastCommon")
            
            return Integer.parseInt(oxygen, 2) * Integer.parseInt(co2, 2)
        }

        override fun check2ExpectedResult(): Number {
            return 230
        }

    }.execute()

}
