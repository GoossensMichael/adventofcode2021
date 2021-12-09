fun main() {

    object : AoC(day = 1) {
        fun solve(input: List<String>, window: Int): Number {
            val depths = input.stream().mapToInt(Integer::parseInt).toArray()
            var count = 0
            for(i in 0..depths.size-(window + 1)) {
                if (depths[i] < depths[i+window]) {
                    count++
                }
            }

            return count
        }

        override fun part1(input: List<String>): Number {
            return solve(input, 1)
        }

        override fun check1ExpectedResult(): Number {
            return 7
        }

        override fun part2(input: List<String>): Number {
            return solve(input, 3)
        }

        override fun check2ExpectedResult(): Number {
            return 5
        }

    }.execute()

}
