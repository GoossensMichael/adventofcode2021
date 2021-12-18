import java.lang.Math.abs

fun main() {

    data class TargetArea(val x: Pair<Int, Int>, val y: Pair<Int, Int>)

    object : AoC(day = 17) {

        // Solving the reverse of Gauss's formula n(n+1)/2 = d
        // i.e. the quadratic equation n^2 + n - d = 0
        // Solution is optimised
//        fun solveQE(d: Int): Double {
//            return (-1.0 + sqrt(1.0 + (8.0 * d))) / 2
//        }

        private fun gauss(n: Int): Int {
            return n * (n + 1) / 2
        }

        override fun part1(input: List<String>): Number {
            val targetArea = input.parse()

            return gauss(abs(targetArea.y.first) - 1)
        }

        override fun check1ExpectedResult(): Number {
            return 45
        }

        override fun part2(input: List<String>): Number {

            val targetArea = input.parse()

            val vyMap = mutableMapOf<Int, MutableList<Int>>()
            for (velocity in -400..400) {

                var overshoot = false
                var y = 0
                var vy = velocity
                var steps = 0

                while (!overshoot) {
                    steps++
                    y += vy
                    vy--

                    if (y < targetArea.y.first) {
                        overshoot = true
                        //println("overshot for velocity $velocity")
                    } else if (y >= targetArea.y.first && y <= targetArea.y.second) {
                        val orDefault = vyMap.getOrDefault(steps, mutableListOf())
                        orDefault.add(velocity)
                        vyMap[steps] = orDefault
                    }
                }
            }

            val vxMap = mutableMapOf<Int, MutableList<Int>>()
            for (velocity in 0..targetArea.x.second) {

                var overshoot = false
                var x = 0
                var vx = velocity
                var steps = 0

                while (!overshoot && vx > 0) {
                    steps++
                    x += vx
                    vx--

                    if (x > targetArea.x.second) {
                        overshoot = true
                    } else if (x >= targetArea.x.first && x <= targetArea.x.second) {
                        val orDefault = vxMap.getOrDefault(steps, mutableListOf())
                        orDefault.add(velocity)
                        vxMap[steps] = orDefault

                        if (vx == 0) {
                            IntRange(steps + 1, 400).forEach { s ->
                                val default = vxMap.getOrDefault(s, mutableListOf())
                                default.add(velocity)
                                vxMap[s] = default
                            }
                        }
                    }
                }
            }

            val result = vxMap.flatMap { vx ->
                vx.value.flatMap { vxValue ->
                    if (vyMap[vx.key] != null) {
                        vyMap[vx.key]!!.map { Pair(vxValue, it) }
                    } else {
                        emptyList()
                    }
                }
            }.toSet()

            return result.size
        }

        override fun check2ExpectedResult(): Number {
            return 112
        }

        private fun List<String>.parse(): TargetArea {
            val targetArea = this.get(0).substring("target area: ".length).split(", ")

            val x = targetArea[0].substring("x=".length).split("..").map { Integer.parseInt(it) }
            val y = targetArea[1].substring("y=".length).split("..").map { Integer.parseInt(it) }

            return TargetArea(Pair(x[0], x[1]), Pair(y[0], y[1]))
        }
    }.execute()

}
