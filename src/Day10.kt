import java.util.*

fun main() {

    object : AoC(day = 10) {

        val pairs = mapOf(
            Pair('(', ')'),
            Pair('[', ']'),
            Pair('{', '}'),
            Pair('<', '>')
        )

        override fun part1(input: List<String>): Number {
            val points = mapOf(
                Pair(')', 3),
                Pair(']', 57),
                Pair('}', 1197),
                Pair('>', 25137)
            )

            return input.asSequence()
                .map {
                    val s = Stack<Char>()

                    var result = 0
                    for (c in it) {
                        when (c) {
                            '(', '<', '{', '[' -> s.push(c)
                            else -> {
                                if (c != pairs[s.peek()]) {
                                    result = points[c] ?: 0
                                    break
                                }
                                s.pop()
                            }
                        }
                    }

                    result
                }.sum()

        }

        override fun check1ExpectedResult(): Number {
            return 26397
        }

        override fun part2(input: List<String>): Number {
            val points = mapOf(
                Pair(')', 1L),
                Pair(']', 2L),
                Pair('}', 3L),
                Pair('>', 4L)
            )

            val sorted = input.asSequence()
                .map {
                    val s = Stack<Char>()

                    for (c in it) {
                        when (c) {
                            '(', '<', '{', '[' -> s.push(c)
                            else -> {
                                if (c != pairs[s.peek()]) {
                                    s.clear()
                                    break
                                }
                                s.pop()
                            }
                        }
                    }

                    s
                }.map {
                    it.reversed()
                        .fold(0L) { acc, i -> (acc * 5L) + (points[pairs[i]] ?: 0L) }
                }.filter { it != 0L }
                .sorted().toList()

            return sorted[sorted.size / 2]
        }

        override fun check2ExpectedResult(): Number {
            return 288957L
        }

    }.execute()

}
