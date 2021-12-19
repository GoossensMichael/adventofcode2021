import java.util.*
import kotlin.math.ceil
import kotlin.math.floor

fun main() {

    data class SnailPart(
        val level: Int, var left: Any? = null, var right: Any? = null,
        var leftNeighbour: SnailPart? = null, var rightNeighbour: SnailPart? = null
    )

    object : AoC(day = 18) {
        private fun show(content: List<Any>) = println(content.map { if (it is IntArray) "" + it[0] + "," + it[1] else it }.joinToString(""))

        override fun part1(input: List<String>): Number {

            return solve(input)
        }

        private fun solve(input: List<String>): Int {
            val snailNumbers = input.parse()

            var current = snailNumbers[0]
            for (i in 1 until snailNumbers.size) {
                val addition = add(current, snailNumbers[i])
                current = reduce(addition)
//p                print("= ")
//p                show(current)
//p                println()
            }

            val magnitude = magnitude(current)
//p            println(magnitude)
            return magnitude
        }

        private fun magnitude(current: List<Any>): Int {
            val magnitudes = Stack<Int>()

            for (c in current) {
                if (c == "]") {
                    magnitudes.push((magnitudes.pop() * 2) + (magnitudes.pop() * 3))
                } else if (c is IntArray) {
                    magnitudes.push(c[0])
                    magnitudes.push(c[1])
                } else if (c is String && c.all { it.isDigit() }) {
                    magnitudes.push(c.toInt())
                }
            }

            return magnitudes.pop()
        }

        private fun reduce(input: List<Any>): List<Any> {
            var loop = true
            var reduced = input
//p            print("Starting to reduce: ")
//p            println(reduced.map { if (it is IntArray) "" + it[0] + "," + it[1] else it }.joinToString(""))
            while (loop) {
                val (hasExploded, exploded) = explode(reduced)
                if (hasExploded) {
                    reduced = exploded
//p                    print("After explode: ")
//p                    println(reduced.map { if (it is IntArray) "" + it[0] + "," + it[1] else it }.joinToString(""))
                } else {
                    val (hasSplit, splitted) = split(reduced)
                    if (hasSplit) {
                        reduced = splitted
//p                        print("After split:   ")
//p                        println(reduced.map { if (it is IntArray) "" + it[0] + "," + it[1] else it }.joinToString(""))
                    } else {
                        loop = false
                    }
                }
            }

            return reduced
        }

        private fun explode(input: List<Any>): Pair<Boolean, List<Any>> {
            var level = 0

            var i = 0
            while (level < 5 && i < input.size) {
                when (input[i]) {
                    "[" -> level++
                    "]" -> level--
                }
                i++
            }

            if (level >= 4) {
                val explosion = mutableListOf<Any>()

                while (i < input.size && input[i] !is IntArray) {
                    i++
                }
                val beginExplosion = i - 1
                val endExplosion = i + 2
                val explosionTarget = input[i] as IntArray

                i--
                while (i >= 0 && !(input[i] is IntArray || (input[i] is String && (input[i] as String).all { it.isDigit() }))) {
                    i--
                }
                if (i > 0) {
                    explosion.addAll(input.subList(0, i))
                    if (input[i] is IntArray) {
                        val array = input[i] as IntArray
                        explosion.add(intArrayOf(array[0], array[1] + explosionTarget[0]))
                        explosion.addAll(input.subList(i + 1, beginExplosion))
                        if (explosion.last() == "," && (explosion[explosion.size - 2] as String).all { it.isDigit()}) {
                            explosion.removeLast()
                            val value = (explosion.removeLast() as String).toInt()
                            explosion.add(intArrayOf(value, 0))
                        } else {
                            explosion.add("0")
                        }
                    } else {
                        val sum = ((input[i] as String).toInt() + explosionTarget[0])
                        if (i + 2 == beginExplosion && input[i + 1] == ",") {
                            explosion.add(intArrayOf(sum, 0))
                        } else {
                            explosion.add(sum.toString())
                            explosion.addAll(input.subList(i + 1, beginExplosion))
                            if (explosion.last() == "," && (explosion[explosion.size - 2] as String).all { it.isDigit()}) {
                                explosion.removeLast()
                                val value = (explosion.removeLast() as String).toInt()
                                explosion.add(intArrayOf(value, 0))
                            } else {
                                explosion.add("0")
                            }
                        }
                    }
                } else {
                    explosion.addAll(input.subList(0, beginExplosion))
                    explosion.add("0")
                }

                i = endExplosion
                while (i < input.size && !(input[i] is IntArray || (input[i] is String && (input[i] as String).all { it.isDigit() }))) {
                    i++
                }
                if (i < input.size) {
                    explosion.addAll(input.subList(endExplosion, i))
                    if (input[i] is IntArray) {
                        val array = input[i] as IntArray
                        explosion.add(intArrayOf(array[0] + explosionTarget[1], array[1]))
                    } else {
                        val sum = ((input[i] as String).toInt() + explosionTarget[1])
                        if (i - 1 == endExplosion && input[i - 1] == ",") {
                            explosion.removeLast()
                            explosion.removeLast()
                            explosion.add(intArrayOf(0, sum))
                        } else {
                            explosion.add(sum.toString())
                        }
                    }
                    if (explosion.last() is String && (explosion.last() as String).all{ it.isDigit() } && i + 2 < input.size && input[i+1] == "," && (input[i+2] as String).all{ it.isDigit() } ) {
                        explosion.add(intArrayOf((explosion.removeLast() as String).toInt(), (input[i+2] as String).toInt()))
                        explosion.addAll(input.subList(i + 3, input.size))
                    } else {
                        explosion.addAll(input.subList(i + 1, input.size))
                    }
                } else {
                    if (explosion.last() is String && (explosion.last() as String).all{ it.isDigit() } &&
                        endExplosion + 1 < input.size && input[endExplosion] == "," && (input[endExplosion + 1] as String).all { it.isDigit() } ) {
                        explosion.add(intArrayOf((explosion.removeLast() as String).toInt(), (input[endExplosion + 1] as String).toInt()))
                        explosion.addAll(input.subList(endExplosion + 2, input.size))
                    } else {
                        explosion.addAll(input.subList(endExplosion, input.size))
                    }
                }

                return Pair(true, explosion)
            }

            return Pair(false, input)
        }

        private fun split(input: List<Any>): Pair<Boolean, List<Any>> {
            var i = 0
            var loop = true
            while (i < input.size && loop) {
                loop =
                    if (input[i] is String) {
                        val value = input[i] as String
                        val l = (value.all { it.isDigit() } && (input[i] as String).toInt() >= 10)
                        !l
                    } else if (input[i] is IntArray) {
                        val value = input[i] as IntArray
                        value[0] < 10 && value[1] < 10
                    } else {
                        true
                    }
                i++
            }

            if (i < input.size) {
                i--
                val splitted = mutableListOf<Any>()
                splitted.addAll(input.subList(0, i))
                if (input[i] is String) {
                    val number = (input[i] as String).toInt()
                    val divided = number / 2.0
                    splitted.addAll(listOf("[", intArrayOf(floor(divided).toInt(), ceil(divided).toInt()), "]"))
                } else {
                    val numbers = (input[i] as IntArray)
                    if (numbers[0] >= 10) {
                        val divided = numbers[0] / 2.0
                        splitted.addAll(listOf("[", intArrayOf(floor(divided).toInt(), ceil(divided).toInt()), "]", ",", numbers[1].toString()))
                    } else {
                        val divided = numbers[1] / 2.0
                        splitted.addAll(listOf(numbers[0].toString(), ",", "[", intArrayOf(floor(divided).toInt(), ceil(divided).toInt()), "]"))
                    }
                }
                splitted.addAll(input.subList(i + 1, input.size))

                return Pair(true, splitted)
            }

            return Pair(false, input)
        }

        override fun check1ExpectedResult(): Number {
            return 4140
        }

        fun permutate(input: List<String>): MutableList<List<String>> {
            val result = mutableListOf<List<String>>()
            for (n in input.indices) {
                val restOfInput = mutableListOf<String>()
                restOfInput.addAll(input.subList(0, n))
                restOfInput.addAll(input.subList(n + 1, input.size))

                for (m in restOfInput.indices) {
                    result.add(listOf(input[n], input[m]))
                }
            }

            return result
        }

        override fun part2(input: List<String>): Number {
            val permutations = permutate(input)

            var maxMagintude = 0
            for (p in permutations) {
                val magnitude = solve(p)
                if (magnitude > maxMagintude) {
                    maxMagintude = magnitude
                    println("New magnitude is max $magnitude")
                }
            }
            return maxMagintude
        }

        override fun check2ExpectedResult(): Number {
            return 3993
        }

        private fun add(first: List<Any>, second: List<Any>): List<Any> {
//p            print("  ")
//p            show(first)
//p            print("+ ")
//p            show(second)
            val newList = mutableListOf<Any>()

            newList.add("[")
            newList.addAll(first)
            newList.add(",")
            newList.addAll(second)
            newList.add("]")

            return newList
        }

        private fun List<String>.parse(): List<List<Any>> {
            return this.map { parseSnailNumber2(it) }
        }

        private fun parseSnailNumber2(input: String): List<Any> {
            val list = mutableListOf<Any>()

            var buffer = ""
            for (c in input) {
                when (c) {
                    '[', ']' -> {
                        if (buffer != "") {
                            if (buffer.last().isDigit()) {
                                if (buffer.contains(',')) {
                                    list.add(buffer.split(',').map { Integer.parseInt(it) }.toIntArray())
                                } else {
                                    list.add(buffer)
                                }
                            } else {
                                list.add(buffer.substring(0, buffer.length - 1))
                                list.add(buffer.substring(buffer.length - 1))
                            }
                            buffer = ""
                        }
                        list.add(c.toString())
                    }
                    ',' -> if (buffer.isEmpty()) {
                        list.add(c.toString())
                    } else {
                        buffer += c
                    }
                    else -> {
                        buffer += c
                    }

                }
            }

            return list.toList()
        }

        private fun parseSnailNumber(input: String): SnailPart {
            val stack = Stack<SnailPart>()

            var current = SnailPart(stack.size)
            stack.push(current)
            for (c in input.substring(1 until input.length - 1)) {
                when (c) {
                    '[' -> {
                        stack.push(current)
                        current = SnailPart(stack.size)
                        stack.peek().left = current
                    }
                    ']' -> {
                        stack.peek().right = current
                        current = stack.pop()
                    }
                    ',' -> {

                    }
                    // Assumes reduced snail numbers!
                    in listOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9') -> {
                        if (current.left == null) {
                            current.left = c.digitToInt()
                        } else {
                            current.right = c.digitToInt()
                        }
                    }

                }
            }

            return current
        }

    }.execute()

}
