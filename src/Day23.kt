import kotlin.math.abs

fun main() {

    // Represents an amphipod at a certain point in time
    // Possible rooms are A, B, C and D for the amphipods and H for the hallway
    data class Amphipod(val type: Char, val room: Char, val position: Int, val energy: Int = 0)
    // Represent the entry points in the hallway for a given room
    val entryPoints = mapOf(Pair('A', 2), Pair('B', 4), Pair('C', 6), Pair('D', 8))
    // Represent the cost in energy for one step per type of amphipod
    val energyCost = mapOf(Pair('A', 1), Pair('B', 10), Pair('C', 100), Pair('D', 1000))
    // Debug flag to print out the current problem state
    val print = false

    object : AoC(day = 23) {
        override fun part1(input: List<String>): Number {
            return solve(input)
        }

        override fun check1ExpectedResult(): Number {
            return 12521
        }

        override fun part2(input: List<String>): Number {
            return solve(input.subList(0, 3) + "  #D#C#B#A#" + "  #D#B#A#C#" + input.subList(3, input.size))
        }

        override fun check2ExpectedResult(): Number {
            return 44169
        }

        private fun isSolved(amphipod: Amphipod, others: Set<Amphipod>): Boolean {
            val slots = (others.size + 1) / 4

            return if (amphipod.type != amphipod.room) {
                false
            } else if (amphipod.position == slots) {
                true
            } else {
                // All slots below are also taken by the same type of amphipod
                (amphipod.position + 1 .. slots).all { p -> others.any { o -> o.room == amphipod.room && o.type == amphipod.type && o.position == p } }
            }

        }

        private fun solve(input: List<String>): Int {
            val amphipods = input.parse()

            return solve(amphipods, mutableSetOf(amphipods), mutableSetOf())
                .minOf { it.sumOf { amphipod -> amphipod.energy } }
        }

        // Main algorithm that splits out the problem space
        private fun solve(amphipods: Set<Amphipod>, history: MutableSet<Set<Amphipod>>, deadState: MutableSet<Set<Amphipod>>): Set<Set<Amphipod>> {
            if (isTargetState(amphipods)) {
                return setOf(amphipods)
            }

            val result = mutableSetOf<Set<Amphipod>>()
            for (amphipod in amphipods.filter { !isSolved(it, amphipods.minus(it)) }) {
                val states = getAllStates(amphipod, amphipods.minus(amphipod))
                if (states.isNotEmpty()) {
                    val others = amphipods.minus(amphipod)

                    for (it in states) {
                        val newAmphipods = others + it
                        if (!history.contains(newAmphipods)) {
                            history.add(newAmphipods)
                            result.addAll(solve(others + it, history, deadState))
                        }
                    }
                }
            }

            if (result.isEmpty()) {
                // Record dead state
                deadState.add(amphipods.map { Amphipod(it.type, it.room, it.position) }.toSet())
            }

            return result.toSet()
        }

        private fun canMoveToRoom(amphipod: Amphipod, hallway: List<Amphipod>, wantsToEnterRoom: Boolean): Boolean {
            return if (wantsToEnterRoom) {
                val steps = minOf(amphipod.position, entryPoints[amphipod.type]!!)..maxOf(amphipod.position, entryPoints[amphipod.type]!!)
                hallway.none { steps.contains(it.position) }
            } else {
                false
            }
        }

        private fun getAllStates(amphipod: Amphipod, others: Set<Amphipod>): Set<Amphipod> {
            val slots = (others.size + 1) / 4
            val energy by lazy { energyCost[amphipod.type]!! }
            val entryPointTargetRoom by lazy { entryPoints[amphipod.type]!! }
            val hallway by lazy { others.filter { it.room == 'H' } }
            // When the room is not blocked and no other species are in the room
            val wantsToEnterRoom by lazy { others.none { amphipod.type == it.room && it.type != amphipod.type } }

            fun moveOutOfRoom(): Set<Amphipod> {
                val exitPoint = entryPoints[amphipod.room]!!
                // Try to move out of the room if possible
                val set = mutableSetOf<Amphipod>()
                // Get leftmost and rightmost position possible for a given entrypoint
                val leftMost = (hallway.filter { it.position < exitPoint }.maxOfOrNull { it.position } ?: -1) + 1
                val rightMost = (hallway.filter { it.position > exitPoint }.minOfOrNull { it.position } ?: 11) - 1
                val hallwayRange = leftMost..rightMost

                if (hallwayRange.contains(entryPointTargetRoom) && canMoveToRoom(amphipod, hallway, wantsToEnterRoom)) {
                    val moves = amphipod.position + abs(entryPoints[amphipod.room]!! - entryPointTargetRoom)
                    val targetPosition = slots - others.count { it.room == amphipod.type }
                    set.add(Amphipod(amphipod.type, amphipod.type, targetPosition,(moves + targetPosition) * energy))
                } else {
                    for (i in leftMost..rightMost) {
                        if (!entryPoints.containsValue(i)) {
                            // Only allow positions not in front of an entry point
                            val moves = amphipod.position + abs(exitPoint - i)
                            set.add(Amphipod(amphipod.type, 'H', i, amphipod.energy + (moves * energy)))
                        }
                    }
                }

                return set.toSet()
            }

            // Handy for debugging by showing a printout of the state
            if (print) {
                print(others + amphipod)
            }

            return if (amphipod.type == amphipod.room) {
                // All cases when an amphipod is in its own room
                if (isSolved(amphipod, others)) {
                    // An amphipod does not move once it is in its own room on position 2
                    emptySet()
                } else /* i.e.: if not solved */ {
                    moveOutOfRoom()
                }
            } else if (amphipod.room != 'H') {
                // Not in the hallway but in a room that is not the target room of the amphipod and can leave right away
                // This means is in position 1 or in position 2 with no other one at position 1
                if (amphipod.position == 1 || others.none { it.room == amphipod.room && it.position < amphipod.position }) {
                    moveOutOfRoom()
                } else {
                    emptySet()
                }
            } else {
                // All cases where the amphipod is in the hallway
                if (canMoveToRoom(amphipod, hallway, wantsToEnterRoom)) {
                    val moves = abs(amphipod.position - entryPointTargetRoom)
                    // Speeding up placement when it is possible to occupy position two at once
                    val position = slots - others.count { it.room == amphipod.type }
                    setOf(Amphipod(amphipod.type, amphipod.type, position, amphipod.energy + ((moves + position) * energy)))
                } else {
                    emptySet()
                }
            }
        }

        private fun isTargetState(amphipods: Set<Amphipod>) = amphipods.all { it.type == it.room }

        private fun List<String>.parse(): Set<Amphipod> {
            val set = mutableSetOf<Amphipod>()
            this.subList(2, this.size - 1).forEachIndexed { i, state ->
                set.add(Amphipod(state[3], 'A', i + 1))
                set.add(Amphipod(state[5], 'B', i + 1))
                set.add(Amphipod(state[7], 'C', i + 1))
                set.add(Amphipod(state[9], 'D', i + 1))
            }

            return set
        }

        private fun print(amphipods: Set<Amphipod>) {
            val slots = amphipods.size / 4

            val h = (0..10).map { n -> amphipods.firstOrNull { it.room == 'H' && it.position == n }?.type ?: '.' }
            val a1 = amphipods.firstOrNull { it.room == 'A' && it.position == 1 }?.type ?: '.'
            val a2 = amphipods.firstOrNull { it.room == 'A' && it.position == 2 }?.type ?: '.'
            val a3 = amphipods.firstOrNull { it.room == 'A' && it.position == 3 }?.type ?: '.'
            val a4 = amphipods.firstOrNull { it.room == 'A' && it.position == 4 }?.type ?: '.'
            val b1 = amphipods.firstOrNull { it.room == 'B' && it.position == 1 }?.type ?: '.'
            val b2 = amphipods.firstOrNull { it.room == 'B' && it.position == 2 }?.type ?: '.'
            val b3 = amphipods.firstOrNull { it.room == 'B' && it.position == 3 }?.type ?: '.'
            val b4 = amphipods.firstOrNull { it.room == 'B' && it.position == 4 }?.type ?: '.'
            val c1 = amphipods.firstOrNull { it.room == 'C' && it.position == 1 }?.type ?: '.'
            val c2 = amphipods.firstOrNull { it.room == 'C' && it.position == 2 }?.type ?: '.'
            val c3 = amphipods.firstOrNull { it.room == 'C' && it.position == 3 }?.type ?: '.'
            val c4 = amphipods.firstOrNull { it.room == 'C' && it.position == 4 }?.type ?: '.'
            val d1 = amphipods.firstOrNull { it.room == 'D' && it.position == 1 }?.type ?: '.'
            val d2 = amphipods.firstOrNull { it.room == 'D' && it.position == 2 }?.type ?: '.'
            val d3 = amphipods.firstOrNull { it.room == 'D' && it.position == 3 }?.type ?: '.'
            val d4 = amphipods.firstOrNull { it.room == 'D' && it.position == 4 }?.type ?: '.'
            println("#############")
            println("#${h.joinToString("")}#")
            println("###$a1#$b1#$c1#$d1###")
            println("  #$a2#$b2#$c2#$d2#")
            if (slots > 2) {
                println("  #$a3#$b3#$c3#$d3#")
                println("  #$a4#$b4#$c4#$d4#")
            }
            println("  #########")
            println("")
        }

    }.execute()

}
