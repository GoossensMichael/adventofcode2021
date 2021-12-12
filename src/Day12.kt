fun main() {

    object : AoC(day = 12) {

        fun parse(input: List<String>): Map<String, List<String>> {
            return input.flatMap {
                val path = it.split('-')
                listOf(path, path.reversed())
            }.groupBy({ it[0] }, { it[1] })
        }

        fun solve(map: Map<String, List<String>>, path: List<String>, visitFilter: (String, List<String>) -> Boolean):
                List<List<String>> {
            val cave = path.last()

            return if (cave == "end") {
                listOf(path)
            } else {
                map[cave]!!.filter {
                    visitFilter(it, path)
                }.flatMap { solve(map, path + it, visitFilter) }
            }
        }

        private fun smallCavesAtMostOnce() = { nextCave: String, path: List<String> ->
            if (nextCave[0].isLowerCase()) {
                !path.contains(nextCave)
            } else {
                true
            }
        }

        override fun part1(input: List<String>): Number {
            val caveMap = parse(input)

            return solve(caveMap, listOf("start"), smallCavesAtMostOnce()).size
        }

        override fun check1ExpectedResult(): Number {
            // test data 2 - 19
            // test data 3 - 226
            return 10
        }

        private fun oneSmallCaveAtMostTwiceOtherSmallCavesOnlyOnce() = {
                nextCave: String, path: List<String> ->
            if (nextCave == "start") {
                false
            } else {
                val count = path.count { p -> nextCave == p }
                if (nextCave[0].isLowerCase()) {
                    // Or the small cave has not been visited yet
                    count == 0 ||
                            // Or nextCave has been visited but there hasn't been another small cave visited twice yet
                            (count < 2 &&
                                    path.filter { cave -> cave[0].isLowerCase() }
                                        .none { smallCave -> path.count { cave -> cave == smallCave } > 1 })
                } else {
                    true
                }
            }
        }

        override fun part2(input: List<String>): Number {
            val caveMap = parse(input)

            return solve(caveMap, listOf("start"), oneSmallCaveAtMostTwiceOtherSmallCavesOnlyOnce()).size
        }

        override fun check2ExpectedResult(): Number {
            // test data 2 - 103
            // test data 3 - 3509
            return 36
        }

    }.execute()

}
