import java.util.stream.Collectors
import kotlin.math.abs

fun main() {

    val day = 9

    fun toHeightArray(input: List<String>): Array<IntArray> {
        val heights = Array(input.size) { IntArray(input[0].length) { 0 } }

        for ((i, line) in input.withIndex()) {
            for (j in line.indices) {
                heights[i][j] = line[j].digitToInt()
            }
        }
        return heights
    }

    fun part1(input: List<String>): Int {
        val heights = toHeightArray(input)

        fun isSmaller(value: Int, heights: Array<IntArray>, i: Int, j: Int): Boolean {
            if (i > heights.size - 1 || j > heights[0].size - 1 || i < 0 || j < 0) {
                return true
            }

            return value < heights[i][j]
        }

        var sum = 0
        for (i in heights.indices) {
            for (j in 0 until heights[0].size) {
                val cur = heights[i][j]
                if (isSmaller(cur, heights, i, j + 1) &&
                    isSmaller(cur, heights, i, j - 1) &&
                    isSmaller(cur, heights, i + 1, j) &&
                    isSmaller(cur, heights, i - 1, j)
                ) {
                    sum += 1 + cur
                }
            }
        }

        return sum
    }

    data class Basin(val coordinates: MutableSet<Pair<Int, Int>> = mutableSetOf()) {

        fun isPartOf(coordinate: Pair<Int, Int>, heights: Array<IntArray>): Boolean {
            return coordinates.stream()
                .anyMatch {
                    val x = abs(it.first - coordinate.first)
                    val y = abs(it.second - coordinate.second)
                    x + y == 1 && canReach(coordinate, it, heights)
                }
        }

        fun canReach(source: Pair<Int, Int>, target: Pair<Int, Int>, heights: Array<IntArray>): Boolean {
            return heights[source.first][target.second] != 9 || heights[target.first][source.second] != 9
        }

    }

    fun part2(input: List<String>): Int {
        val heights = toHeightArray(input)

        val basins = mutableListOf<Basin>()
        for (i in heights.indices) {
            for (j in 0 until heights[0].size) {
                if (heights[i][j] != 9) {
                    val coordinate = Pair(i, j)
                    val basinsForCoordinate = basins.stream()
                        .filter { it.isPartOf(coordinate, heights) }
                        .collect(Collectors.toList())

                    val basin =
                        if (basinsForCoordinate.size > 1) {
                            // merge basins
                            val mergedBasin = basinsForCoordinate.stream()
                                .collect(
                                    { Basin() },
                                    { a, b -> a.coordinates.addAll(b.coordinates) },
                                    { a, b -> a.coordinates.addAll(b.coordinates) })
                            basins.removeAll { basinsForCoordinate.contains(it) }
                            basins.add(mergedBasin)
                            mergedBasin
                        } else if (basinsForCoordinate.size == 1) {
                            // only one basin fits
                            basinsForCoordinate[0]
                        } else {
                            // no basin found yet
                            val newBasin = Basin()
                            basins.add(newBasin)
                            newBasin
                        }
                    basin.coordinates.add(coordinate)
                }
            }
        }

        val biggestBassins = basins.stream()
            .sorted { first, second -> second.coordinates.size - first.coordinates.size }
            .collect(Collectors.toList())
            .subList(0, 3)

        return biggestBassins.stream()
            .mapToInt { it.coordinates.size }
            .reduce(1) { a, b -> a * b }
    }

    fun treatPart(part: Int, answer: Int) {
        print("Submit part $part result $answer? (y|n): ")
        if (readLine() == "y") {
            sendAnswer(day, part, answer)
        }
    }

// test if implementation meets criteria from the description for part 1, like:
    val testInput = readInput(String.format("Day%02d_test", day))
    check(part1(testInput) == 15)

// test was ok retrieve the real data
    val input = readInput(String.format("Day%02d", day))

// get the answer with the real data for part 1
    treatPart(1, part1(input))

// test if implementation meets criteria from the description for part 2, like:
    check(part2(testInput) == 1134)

// get the answer with the real data for part 2
    treatPart(2, part2(input))
}
