import java.util.stream.Collectors

fun main() {

    val day = 8

    fun part1(input: List<String>): Int {
        val sum = input.stream()
            .map { it.split(" | ") }
            .map { it[1] }
            .map { it.split(' ').groupBy { it.length } }
            .flatMap { it.entries.stream() }
            .filter { it.key == 2 || it.key == 4 || it.key == 3 || it.key == 7 }
            .mapToInt { it.value.size }
            .sum()

        return sum
    }

    fun minus(numbers: List<String>, number: String): MutableMap<Int, MutableList<Pair<String, Int>>> {
        val collect = numbers.stream()
            .map {
                Pair(it, it.toCharArray()
                    .filter { c -> !number.contains(c) }
                    .joinToString("")
                    .length)
            }
            .collect(Collectors.groupingBy({ it.second }, Collectors.toList()))

        return collect
    }

    fun solve2(input: Pair<Map<Int, List<String>>, List<String>>): Int {
        val codes = input.first
        val numbers = mutableMapOf<String, String>()

        val one = codes.get(2)!!.get(0)
        val four = codes.get(4)!!.get(0)
        val seven = codes.get(3)!!.get(0)
        val eight = codes.get(7)!!.get(0)

        val grp235 = codes.get(5)!!
        val three = minus(grp235, one).get(3)!!.stream().findFirst().orElseThrow().first
        val two = minus(grp235, four).get(3)!!.stream().findFirst().orElseThrow().first
        val five = minus(grp235, two).get(2)!!.stream().findFirst().orElseThrow().first

        val grp069 = codes.get(6)!!
        val six = minus(grp069, one).get(5)!!.stream().findFirst().orElseThrow().first
        val nine = minus(grp069, four).get(2)!!.stream().findFirst().orElseThrow().first
        val zero = minus(grp069, five).get(2)!!.stream().findFirst().orElseThrow().first

        numbers[zero] = "0"
        numbers[one] = "1"
        numbers[two] = "2"
        numbers[three] = "3"
        numbers[four] = "4"
        numbers[five] = "5"
        numbers[six] = "6"
        numbers[seven] = "7"
        numbers[eight] = "8"
        numbers[nine] = "9"

        val result = input.second.stream()
            .map { numbers[it] }
            .collect(Collectors.joining())


        println(result)
        return Integer.parseInt(result)
    }

    fun part2(input: List<String>): Int {
        return input.stream()
            .map { it.split(" | ") }
            .map { Pair(
                it[0].split(' ').map { it.toCharArray().sorted().joinToString("") }.groupBy { it.length },
                it[1].split(' ').map { it.toCharArray().sorted().joinToString("") })
            }
            .peek { println(it.first) }
            .mapToInt { solve2(it) }
            .sum()
    }

    fun treatPart(part: Int, answer: Int) {
        print("Submit part $part result $answer? (y|n): ")
        if (readLine() == "y") {
            sendAnswer(day, part, answer)
        }
    }

    // test if implementation meets criteria from the description for part 1, like:
    val testInput = readInput(String.format("Day%02d_test", day))
    check(part1(testInput) == 26)

    // test was ok retrieve the real data
    val input = readInput(String.format("Day%02d", day))

    // get the answer with the real data for part 1
    treatPart(1, part1(input))

    // test if implementation meets criteria from the description for part 2, like:
    check(part2(testInput) == 61229)

    // get the answer with the real data for part 2
    treatPart(2, part2(input))
}
