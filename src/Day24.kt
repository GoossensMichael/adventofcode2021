fun main() {

    object : AoC(day = 24) {

        override fun part1(input: List<String>): Number {

            var states = mutableMapOf(Pair("w=0,x=0,y=0,z=0,", Pair(0L, 0L)))
//            var states = mutableMapOf(Pair(mapOf(Pair('w', 0), Pair('x', 0), Pair('y', 0), Pair('z', 0)), Pair(0L, 0L)))

            fun b(state: String, bHolder: String) =
                if (bHolder.removePrefix("-").all { it.isDigit() }) {
                    bHolder.toLong()
                } else {
                    val b = bHolder[0]
                    val newStateBegin = state.indexOf(b)
                    val newStateEnd = state.substring(newStateBegin).indexOf(',')
                    state.substring(newStateBegin, newStateBegin + newStateEnd).split('=')[1].toLong()
                }

            val begin = System.currentTimeMillis()
            for (instruction in input.sanitize()) {
                val split = instruction.split(' ')
                val operation = split[0]
                val a = split[1][0]
                when (operation) {
                    "inp" -> {
                        val newStates = mutableMapOf<String, Pair<Long, Long>>()

                        var merged = 0

                        for (state in states) {
                            for (n in 9 downTo 1) {
                                val (lo, hi) = state.value
                                val wEnd = state.key.indexOf(',')
                                val newState = Pair("w=$n" + state.key.substring(wEnd), Pair((lo * 10L) + n, (hi * 10L) + n))
                                // w = n
//                                newState.first[a] = n

                                if (newStates.contains(newState.first)) {
                                    merged ++
                                    val curHiLo = newStates[newState.first]!!
                                    val newHiLo = newState.second
                                    newStates[newState.first] =
                                        Pair(minOf(newHiLo.first, curHiLo.first), maxOf(newHiLo.second, curHiLo.second))
                                } else {
                                    newStates.put(newState.first, newState.second)
                                }

                            }
                        }
                        states = newStates
                        print("Processing ${states.size} states. $merged merged.")
                        println("Time since start: ${(System.currentTimeMillis() - begin) / 1000.0} s")
                    }
                    else -> {
                        val newStates = mutableMapOf<String, Pair<Long, Long>>()
                        val stateTrans = mapOf<String, String>()
                        for (state in states) {
                            if (stateTrans.contains(state.key)) {
                                newStates.put(stateTrans[state.key]!!, state.value)
                                continue
                            }
                            val newStateBegin = state.key.indexOf(a)
                            val newStateEnd = state.key.substring(newStateBegin).indexOf(',')
                            val bValue = b(state.key, split[2])
                            val aValue = state.key.substring(newStateBegin, newStateBegin + newStateEnd).split('=')[1].toLong()
                            var newState = state.key.substring(0, newStateBegin)
                            when (operation) {
                                "add" -> newState += "$a=" + (aValue + bValue)
                                "mul" -> newState += "$a=" + (aValue * bValue)
                                "div" -> {
                                    if (bValue == 0L) {
                                        error("INvalid mod value $bValue can not be zero for division.")
                                    }
                                    newState += "$a=" + (aValue / bValue)
                                }
                                "mod" -> {
                                    if (aValue < 0L || bValue <= 0L) {
                                        error("INvalid mod value $aValue can not be less than 0 or $bValue can not be zero or less for modulo.")
                                    }
                                    newState += "$a=" + (aValue % bValue)
                                }
                                "eql" -> newState += "$a=" + if (aValue == bValue) 1 else 0
                                else -> error("Not supported instruction $instruction")
                            }
                            newState += state.key.substring(newStateBegin + newStateEnd)
                            newStates.put(newState, state.value)
                        }
                        states = newStates
                    }
                }
            }

            println("Algo done: ${(System.currentTimeMillis() - begin) / 1000.0} s")
            return 0L
        }

        override fun check1ExpectedResult(): Number {
            return 0L
        }

        override fun part2(input: List<String>): Number {
            return 0
        }

        override fun check2ExpectedResult(): Number {
            return 0
        }

        // Removing useless instructions
        private fun List<String>.sanitize(): List<String> {
            return this.filter {
                val split = it.split(' ')

                !(
                        ((split[0] == "div" || split[0] == "mul") && split[2].length == 1 && split[2][0] == '1') ||
                                (split[0] == "add" && split[2].length == 1 && split[2][0] == '0')
                        )
            }
        }

    }.execute()
}
