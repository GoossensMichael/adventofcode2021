import java.util.*
import kotlin.math.abs

enum class CubeState { ON, OFF }

fun main() {

    data class Cuboid(val x: IntRange, val y: IntRange, val z: IntRange) {
        fun intersects(other: Cuboid): Boolean {
            return x.last >= other.x.first && x.first <= other.x.last &&
                    y.last >= other.y.first && y.first <= other.y.last &&
                    z.last >= other.z.first && z.first <= other.z.last
        }

        fun amountOfCubes() = (abs(x.last.toLong() - x.first.toLong()) + 1L) *
                (y.last.toLong() - y.first.toLong() + 1L) *
                (z.last.toLong() - z.first.toLong() + 1L)

        fun isInInitializationProcedureArea() = abs(x.first) <= 50 && abs(x.last) <= 50 &&
                    abs(y.first) <= 50 && abs(y.last) <= 50 &&
                    abs(z.first) <= 50 && abs(z.last) <= 50
    }

    data class RebootStep(val state: CubeState, val cuboid: Cuboid)

    object : AoC(day = 22) {
        override fun part1(input: List<String>): Number {
            return solve(input.parse().filter { it.cuboid.isInInitializationProcedureArea() })
        }

        override fun check1ExpectedResult(): Number {
            // test: 474140
            // test2: 39
            // test3: 39 - same as test2 but negative
            // test4: 590784
            return 474140L
        }

        override fun part2(input: List<String>): Number {
            return solve(input.parse())
        }

        override fun check2ExpectedResult(): Number {
            return 2758514936282235L
        }

        private fun List<String>.parse(): List<RebootStep> {
            return this.map {
                val split = it.split(' ')
                val state = if (split[0] == "on") CubeState.ON else CubeState.OFF
                val (x, y, z) = split[1].split(',')
                    .map { cr -> cr.substring(2).split("..") }
                    .map { r -> IntRange(r[0].toInt(), r[1].toInt()) }
                RebootStep(state, Cuboid(x, y, z))
            }
        }

        private fun solve(rebootSteps: List<RebootStep>): Long {
            var cuboids = setOf<Cuboid>()

            for (rebootStep in rebootSteps) {
                cuboids = executeRebootStep(rebootStep, cuboids)
            }

            return cuboids.sumOf { it.amountOfCubes() }
        }

        private fun executeRebootStep(rebootStep: RebootStep, cuboids: Set<Cuboid>): Set<Cuboid> {
            val newCuboids = mutableSetOf<Cuboid>()
            if (rebootStep.state == CubeState.ON) {
                // Reboot work to be done is on the stack
                val stack = Stack<Cuboid>()
                stack.add(rebootStep.cuboid)
                // Go through all cuboids to find intersections
                val iter = cuboids.iterator()
                while (!stack.isEmpty() || iter.hasNext()) {
                    if (stack.isEmpty()) {
                        // The rebootstep has completely been absorbed
                        while (iter.hasNext()) { newCuboids.add(iter.next()) }
                    } else if (!iter.hasNext()) {
                        // All leftover reboot cuboids can be added, they are representing extra ON cubes
                        newCuboids.addAll(stack)
                        stack.clear()
                    } else {
                        // Basically filter out all that are already ON
                        val rebootCuboids = stack.toList()
                        stack.clear()

                        val currentCuboid = iter.next()
                        for (rebootCuboid in rebootCuboids) {
                            if (currentCuboid.intersects(rebootCuboid)) {
                                // Intersection found, the leftover cuboids of the rebootCuboid are kept to continue
                                val (_, _, b) = slice(currentCuboid, rebootCuboid)
                                b.forEach { stack.push(it) }
                            } else {
                                // No intersection found, rebootCuboid stays as is
                                stack.add(rebootCuboid)
                            }
                        }

                        newCuboids.add(currentCuboid)
                    }
                }
            } else {
                for (cuboid in cuboids) {
                    if (cuboid.intersects(rebootStep.cuboid)) {
                        val (_, a, _) = slice(cuboid, rebootStep.cuboid)
                        newCuboids.addAll(a)
                    } else {
                        newCuboids.add(cuboid)
                    }
                }

            }
            return newCuboids
        }

        /*
         * Slices the cuboid a with the cuboid b. Doing so returns three sets of cuboids:
         * 1: A set containing all cuboids that were part of a but not of b
         * 2: A set containing all cuboids that were part of b and a
         * 3: A set containing all cuboids that were part of b but not of a
         */
        private fun slice(a: Cuboid, b: Cuboid): Triple<Cuboid?, Set<Cuboid>, Set<Cuboid>> {
            fun intersect(a: Cuboid, b: Cuboid) =
                Cuboid(
                    maxOf(a.x.first, b.x.first)..minOf(a.x.last, b.x.last),
                    maxOf(a.y.first, b.y.first)..minOf(a.y.last, b.y.last),
                    maxOf(a.z.first, b.z.first)..minOf(a.z.last, b.z.last)
                )

            val intersection = intersect(a, b)
            val aWithoutIntersection = removeIntersection(a, intersection)
            val bWithoutIntersection = removeIntersection(b, intersection)

            return Triple(intersection, aWithoutIntersection, bWithoutIntersection)
        }

        private fun removeIntersection(a: Cuboid, intersection: Cuboid): Set<Cuboid> {
            val leftOver = mutableSetOf<Cuboid>()

            // above -> 0 or 1
            // Full x and z range of a
            if (a.y.last > intersection.y.last) {
                leftOver.add(Cuboid(a.x, intersection.y.last + 1..a.y.last, a.z))
            }

            // under -> 0 or 1
            // Full x and z range of a
            if (a.y.first < intersection.y.first) {
                leftOver.add(Cuboid(a.x, a.y.first until intersection.y.first, a.z))
            }

            // same level -> between 0 and 4
            // Left section
            if (a.x.first < intersection.x.first) {
                leftOver.add(Cuboid(a.x.first until intersection.x.first, intersection.y, intersection.z))
            }
            // Right section
            if (a.x.last > intersection.x.last) {
                leftOver.add(Cuboid(intersection.x.last + 1..a.x.last, intersection.y, intersection.z))
            }
            // Front section
            if (a.z.first < intersection.z.first) {
                leftOver.add(Cuboid(a.x, intersection.y, a.z.first until intersection.z.first))
            }
            // Back section
            if (a.z.last > intersection.z.last) {
                leftOver.add(Cuboid(a.x, intersection.y, intersection.z.last + 1..a.z.last))
            }

            return leftOver.toSet()
        }

    }.execute()

}
