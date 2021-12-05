fun main() {

    val day = 5

    fun prepareRange(
        from: List<String>,
        to: List<String>,
        position: Int
    ): IntProgression {
        val i = Integer.parseInt(from[position])
        val j = Integer.parseInt(to[position])

        return if (i > j) { IntRange(j, i).reversed() } else { IntRange(i, j) }
    }

    fun mapOceanFloor(ventLines: List<String>, withDiagonals: Boolean = false): Map<String, Int> {
        val oceanFloor = mutableMapOf<String, Int>()
        for (ventLine in ventLines) {
            val fromAndTo = ventLine.split(" -> ")
            val from = fromAndTo[0].split(',')
            val to = fromAndTo[1].split(',')

            if (from[0] == to[0] || from[1] == to[1]) {
                if (from[0] == to[0]) {
                    val range = prepareRange(from, to, 1)
                    for (y in range) {
                        val key = "${from[0]}_${y}"
                        oceanFloor[key] = oceanFloor.getOrDefault(key, 0) + 1
                    }
                } else {
                    val range = prepareRange(from, to, 0)
                    for (x in range) {
                        val key = "${x}_${from[1]}"
                        oceanFloor[key] = oceanFloor.getOrDefault(key, 0) + 1
                    }
                }
            } else if (withDiagonals) {
                val rangeX = prepareRange(from, to, 0)
                val rangeY = prepareRange(from, to, 1)
                val range = rangeX.zip(rangeY)

                println("${from[0]},${from[1]} to ${to[0]},${to[1]}")
                for (position in range) {
                    println("${position.first},${position.second}")
                    val key = "${position.first}_${position.second}"
                    oceanFloor[key] = oceanFloor.getOrDefault(key, 0) + 1
                }
                println()
            }
        }

        return oceanFloor
    }

    fun part1(input: List<String>): Int {
        val oceanFloor = mapOceanFloor(input)

        return oceanFloor.values.filter { it > 1 }.count()
    }

    fun part2(input: List<String>): Int {
        val oceanFloor = mapOceanFloor(input, true)

        return oceanFloor.values.filter { it > 1 }.count()
    }

    fun treatPart(part: Int, answer: Int) {
        print("Submit part $part result $answer? (y|n): ")
        if (readLine() == "y") {
            sendAnswer(day, part, answer)
        }
    }

    // test if implementation meets criteria from the description for part 1, like:
    val testInput = readInput(String.format("Day%02d_test", day))
    check(part1(testInput) == 5)

    // test was ok retrieve the real data
    val input = readInput(String.format("Day%02d", day))

    // get the answer with the real data for part 1
    treatPart(1, part1(input))

    // test if implementation meets criteria from the description for part 2, like:
    check(part2(testInput) == 12)

    // get the answer with the real data for part 2
    treatPart(2, part2(input))
}
