import java.util.*
import kotlin.system.measureTimeMillis
import kotlin.time.measureTime

fun main() {

    data class PartialSolution(val path: List<Pair<Int, Int>>, var risk: Int)

    class PartialSolutionComparator : kotlin.Comparator<PartialSolution> {
        override fun compare(a: PartialSolution?, b: PartialSolution?): Int {
            if (a == null || b == null) {
                error("Not possible!")
            }

            if (a.risk == b.risk) {
                if (a.path.size != b.path.size) {
                    return a.path.size - b.path.size
                } else {
                    return a.path.map { it.first * 10 + it.second }.sum() - b.path.map { it.first * 10 + it.second }.sum()
                }
            } else {
                return a.risk - b.risk
            }
        }

    }

    object : AoC(day = 15) {

        val comp = PartialSolutionComparator()

        tailrec fun solve(grid: List<String>, partialSolutions: SortedSet<PartialSolution>,
                          visits: MutableMap<Pair<Int, Int>, Int>): PartialSolution {
            val currentSolution = partialSolutions.first()

            return if (currentSolution.path.last() == Pair(grid.size - 1, grid[0].length - 1)) {
                currentSolution
            } else {
                partialSolutions.remove(currentSolution)
                partialSolutions.removeAll(
                    partialSolutions.filter { it.path.last() == currentSolution.path.last() }.toSet())

                val newPartialSolutions = getPartialSolutions(grid, currentSolution, visits)

                newPartialSolutions.forEach { newPartialSolution ->
                    partialSolutions.removeAll(
                        partialSolutions.filter {
                            it.path.last() == newPartialSolution.path.last() && it.risk > newPartialSolution.risk
                        }.toSet()
                    )
                }
                partialSolutions.addAll(newPartialSolutions)

                solve(grid, partialSolutions, visits)
            }
        }

        fun getPartialSolutions(grid: List<String>, current: PartialSolution, visits: MutableMap<Pair<Int, Int>, Int>):
                List<PartialSolution> {

            val position = current.path.last()

            return listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))
                .map { Pair(position.first + it.first, position.second + it.second) }
                .filter { isValidPosition(it, grid) && !current.path.contains(it) }
                .map {
                    PartialSolution(current.path + it, current.risk + grid[it.first][it.second].digitToInt())
                }
                .filter { isBetterVisit(visits, it) }
        }

        private fun isBetterVisit(visits: MutableMap<Pair<Int, Int>, Int>, partialSolution: PartialSolution): Boolean {
            val position = partialSolution.path.last()
            visits[position] = visits.getOrDefault(position, partialSolution.risk)

            return visits[position] == partialSolution.risk
        }

        private fun isValidPosition(
            it: Pair<Int, Int>,
            grid: List<String>
        ) = it.first < grid.size && it.first >= 0 && it.second < grid[0].length && it.second >= 0

        private fun solve(input: List<String>): Number {
            val startPosition = Pair(0, 0)

            val visits = mutableMapOf(Pair(startPosition, 0))
            val partialSolutions = getPartialSolutions(input, PartialSolution(listOf(startPosition), 0), visits)
            val solution = solve(input, sortedSetOf(comp, *partialSolutions.toTypedArray()), visits)

            return solution.risk
        }

        override fun part1(input: List<String>): Number {
            return solve(input)
        }

        override fun check1ExpectedResult(): Number {
            return 40
        }

        private fun List<String>.expand(): List<String> {
            fun expand(input: String, step: Int): String {
                return input.map {
                    if (it.digitToInt() + step <= 9) {
                        it.digitToInt() + step
                    } else {
                        (it.digitToInt() + step) % 9
                    }
                }.joinToString("")
            }

            fun expand(input: List<String>, step: Int): List<String> {
                return input.map { expand(it, step) }
            }

            val expandedHorizontal = this.map { line ->
                (0..4).map { expand(line, it) }.joinToString("")
            }

            return (0..4).flatMap { expand(expandedHorizontal, it) }
        }

        override fun part2(input: List<String>): Number {
            val expandedInput = input.expand()

            return solve(expandedInput)
        }

        override fun check2ExpectedResult(): Number {
            return 315
        }

    }.execute()

}
