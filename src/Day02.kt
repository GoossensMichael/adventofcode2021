import java.lang.RuntimeException

fun main() {

    val day = 2

    fun part1(input: List<String>): Number {
        var x = 0
        var y = 0
        input.stream()
            .map { it.split(' ') }
            .forEach {
                val stepSize = Integer.parseInt(it[1])
                when (it[0]) {
                    "forward" -> x += stepSize
                    "down" -> y += stepSize
                    "up" -> y -= stepSize
                    else -> throw RuntimeException("it = $it")
                }
            }

        return x * y
    }

    fun part2(input: List<String>): Number {
        var x = 0
        var y = 0
        var aim = 0
        input.stream()
            .map { it.split(' ') }
            .forEach {
                val stepSize = Integer.parseInt(it[1])
                when (it[0]) {
                    "forward" -> {
                        x += stepSize
                        y += aim * stepSize
                    }
                    "down" -> aim += stepSize
                    "up" -> aim -= stepSize
                    else -> throw RuntimeException("it = $it")
                }
            }

        println("x $x - y $y")
        return x * y
    }

    // test if implementation meets criteria from the description for part 1, like:
    val testInput = readInput(String.format("Day%02d_test", day))
    check(part1(testInput) == 150)

    // test was ok retrieve the real data
    val input = readInput(String.format("Day%02d", day))

    // get the answer with the real data for part 1
    treatPart(1, part1(input), day)

    // test if implementation meets criteria from the description for part 2, like:
    check(part2(testInput) == 900)

    // get the answer with the real data for part 2
    treatPart(2, part2(input), day)
}
