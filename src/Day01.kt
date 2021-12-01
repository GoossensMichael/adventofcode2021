fun main() {

    val day = 1;

    fun part1(input: List<String>): Int {
        val instructions = input.get(0).split(", ").toList()
        var direction = intArrayOf(0, 1)
        var position = intArrayOf(0, 0)

        for(instruction in instructions) {
            val rotation = instruction.get(0)
            val steps = Character.getNumericValue(instruction.get(1))

            if (rotation == 'L') {
                val newDirection = intArrayOf(-direction[1], direction[0])
                direction = newDirection
            } else if (rotation == 'R') {
                val newDirection = intArrayOf(direction[1], -direction[0])
                direction = newDirection
            }

            position[0] = position[0] + (steps * direction[0])
            position[1] = position[1] + (steps * direction[1])
        }

        return Math.abs(position[0]) + Math.abs(position[1])
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    fun treatPart(part: Int, answer: Int) {
        print("Submit part $part result $answer? (y|n): ")
        if (readLine() == "y") {
            sendAnswer(day, part, answer)
        }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput(String.format("Day%02d_test", day))
    check(part1(testInput) == 12)

    val input = readInput(String.format("Day%02d", day))

    treatPart(1, part1(input))
    treatPart(2, part2(input))
}
