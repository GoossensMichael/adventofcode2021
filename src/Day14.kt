fun main() {

    object : AoC(day = 14) {

        private fun parse(input: List<String>): Pair<String, Map<String, Char>> {
            val polymer = input[0]

            val groupBy = input.subList(2, input.size)
                .map { it.split(" -> ") }
                .groupBy({ it[0] }, { it[1][0] })
                .mapValues { it.value[0] }

            return Pair(polymer, groupBy)
        }

        override fun part1(input: List<String>): Number {
            val polymer = solve(input, 10)

            val eachCount = polymer.groupingBy { it }.eachCount()

            val max = eachCount.entries.reduce { acc, entry -> if (acc.value < entry.value) entry else acc }
            val min = eachCount.entries.reduce { acc, entry -> if (acc.value > entry.value) entry else acc }

            return max.value - min.value
        }

        private fun solve(input: List<String>, steps: Int): String {
            val (start, map) = parse(input)

            var polymer = start
            for (step in 1..steps) {
                var newPolymer = ""
                for (i in 2..polymer.length) {
                    val mapping = map[polymer.substring(i - 2, i)]!!
                    newPolymer += polymer[i - 2] + mapping.toString()
                }
                polymer = newPolymer + polymer.last()
            }
            return polymer
        }

        override fun check1ExpectedResult(): Number {
            return 1588
        }

        fun pair(polymer: String): Map<String, Long> {
            val map = mutableMapOf<String, Long>()
            for (i in 2..polymer.length) {
                val key = polymer.substring(i - 2, i)
                val occurences = map.getOrDefault(key, 0L)
                map[key] = occurences + 1
            }

            return map
        }

        fun solve2(input: List<String>, steps: Int): Long {
            val (start, map) = parse(input)

            val byOccurence = start.groupingBy { it }.eachCount().mapValues { it.value.toLong() }.toMutableMap()

            var polymer = pair(start)
            for (step in 1..steps) {
                val newPolymer = mutableMapOf<String, Long>()
                for (p in polymer) {
                    val s = map[p.key]

                    if (s != null) {
                        val left = p.key[0] + s.toString()
                        val lcount = newPolymer.getOrDefault(left, 0L)
                        newPolymer[left] = lcount + p.value

                        val right = s.toString() + p.key[1]
                        val rcount = newPolymer.getOrDefault(right, 0L)
                        newPolymer[right] = rcount + p.value

                        val sCount = byOccurence.getOrDefault(s, 0)
                        byOccurence[s] = sCount + p.value
                    } else {
                        val count = newPolymer.getOrDefault(p.key, 0L)
                        newPolymer[p.key] = count + p.value
                    }

                }
                polymer = newPolymer
            }

            return byOccurence.asSequence().maxOf { it.value } - byOccurence.asSequence().minOf { it.value }
        }

        override fun part2(input: List<String>): Number {
            return solve2(input, 40)
        }

        override fun check2ExpectedResult(): Number {
            return 2188189693529L
        }

    }.execute()

}
