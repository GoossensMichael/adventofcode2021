import java.lang.RuntimeException

fun main() {

    object : AoC(day = 2) {
        override fun part1(input: List<String>): Number {
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

        override fun check1ExpectedResult(): Number {
            return 150
        }

        override fun part2(input: List<String>): Number {
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

            return x * y
        }

        override fun check2ExpectedResult(): Number {
            return 900
        }

    }.execute()

}
