import java.net.http.HttpRequest

fun main() {

    val day = 6

    val FULL_CYCLE = 9

    fun solve(input: List<String>, days: Int): Number {
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

    fun part1(input: List<String>): Number {

        return solve(input, 80)
    }

    fun part2(input: List<String>): Number {
        return solve(input, 256)
    }

    // test if implementation meets criteria from the description for part 1, like:
    val testInput = readInput(String.format("Day%02d_test", day))
    check(part1(testInput) == 5934L)

    // test was ok retrieve the real data
    val input = readInput(String.format("Day%02d", day))

    // get the answer with the real data for part 1
    treatPart(1, part1(input), day)

    // test if implementation meets criteria from the description for part 2, like:
    check(part2(testInput) == 26984457539L)

    // get the answer with the real data for part 2
    treatPart(1, part2(input), day)
}
