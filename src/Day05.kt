fun main() {

    object : AoC(day = 5) {

        private fun prepareRange(
            from: List<String>,
            to: List<String>,
            position: Int
        ): IntProgression {
            val i = Integer.parseInt(from[position])
            val j = Integer.parseInt(to[position])

            return if (i > j) { IntRange(j, i).reversed() } else { IntRange(i, j) }
        }

        private fun mapOceanFloor(ventLines: List<String>, withDiagonals: Boolean = false): Map<String, Int> {
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

                    for (position in range) {
                        val key = "${position.first}_${position.second}"
                        oceanFloor[key] = oceanFloor.getOrDefault(key, 0) + 1
                    }
                }
            }

            return oceanFloor
        }

        override fun part1(input: List<String>): Number {
            val oceanFloor = mapOceanFloor(input)

            return oceanFloor.values.filter { it > 1 }.count()
        }

        override fun check1ExpectedResult(): Number {
            return 5
        }

        override fun part2(input: List<String>): Number {
            val oceanFloor = mapOceanFloor(input, true)

            return oceanFloor.values.filter { it > 1 }.count()
        }

        override fun check2ExpectedResult(): Number {
            return 12
        }

    }.execute()

}
