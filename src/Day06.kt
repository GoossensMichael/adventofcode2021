fun main() {

    object : AoC(day = 6) {
        val FULL_CYCLE = 9

        private fun solve(input: List<String>, days: Int): Number {
            var population = Array(FULL_CYCLE) { i -> 0L }

            input.get(0).split(',')
                .map { Integer.parseInt(it) }
                .forEach { population[it]++ }

            repeat(days) {
                val newPopulation = Array(FULL_CYCLE) { i -> 0L }

                for (period in 1 until FULL_CYCLE) {
                    newPopulation[period - 1] += population[period]
                }
                newPopulation[6] += population[0]
                newPopulation[8] += population[0]
                population = newPopulation
            }

            return population.sum()
        }

        override fun part1(input: List<String>): Number {
            return solve(input, 80)
        }

        override fun check1ExpectedResult(): Number {
            return 5934L
        }

        override fun part2(input: List<String>): Number {
            return solve(input, 256)
        }

        override fun check2ExpectedResult(): Number {
            return 26984457539L
        }

    }.execute()

}
