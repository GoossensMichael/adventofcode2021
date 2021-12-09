import java.util.stream.Collectors
import kotlin.math.abs
import kotlin.streams.asSequence

fun main() {

    object : AoC(day = 9) {

        private fun toHeightArray(input: List<String>): Array<IntArray> {
            val heights = Array(input.size) { IntArray(input[0].length) { 0 } }

            for ((i, line) in input.withIndex()) {
                for (j in line.indices) {
                    heights[i][j] = line[j].digitToInt()
                }
            }
            return heights
        }

        override fun part1(input: List<String>): Number {
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

        override fun check1ExpectedResult(): Number {
            return 15
        }

        override fun part2(input: List<String>): Number {
            val basinAccumulator: (Basin, Basin) -> Unit = { a, b -> a.coordinates.addAll(b.coordinates) }

            val heights = toHeightArray(input)

            val basins = mutableListOf<Basin>()
            for (i in heights.indices) {
                for (j in heights[0].indices) {
                    if (heights[i][j] != 9) {
                        val coordinate = Pair(i, j)
                        val basinsForCoordinate = basins.stream()
                            .filter { it.isPartOf(coordinate, heights) }
                            .collect(Collectors.toList())

                        val basin = when {
                            basinsForCoordinate.size > 1 -> {
                                // merge basins
                                val mergedBasin =
                                    basinsForCoordinate.stream().collect({ Basin() }, basinAccumulator, basinAccumulator)
                                basins.removeAll { basinsForCoordinate.contains(it) }
                                basins.add(mergedBasin)
                                mergedBasin
                            }
                            basinsForCoordinate.size == 1 -> basinsForCoordinate[0]
                            else -> {
                                // no basin found yet
                                val newBasin = Basin()
                                basins.add(newBasin)
                                newBasin
                            }
                        }
                        basin.coordinates.add(coordinate)
                    }
                }
            }

            return basins.stream()
                .asSequence()
                .map { it.coordinates.size }
                .sortedDescending()
                .take(3)
                .reduce { a, b -> a * b }
        }

        override fun check2ExpectedResult(): Number {
            return 1134
        }

    }.execute()

}

data class Basin(val coordinates: MutableSet<Pair<Int, Int>> = mutableSetOf()) {

    fun isPartOf(coordinate: Pair<Int, Int>, heights: Array<IntArray>): Boolean {
        return coordinates.stream()
            .anyMatch {
                val x = abs(it.first - coordinate.first)
                val y = abs(it.second - coordinate.second)
                // manhattan distance is 1 and path is not blocked by a 9
                x + y == 1 && canReach(coordinate, it, heights)
            }
    }

    private fun canReach(source: Pair<Int, Int>, target: Pair<Int, Int>, heights: Array<IntArray>): Boolean {
        return heights[source.first][target.second] != 9 || heights[target.first][source.second] != 9
    }

}
