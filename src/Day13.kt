fun main() {

    data class Paper(val dots: List<Pair<Int, Int>>) {

        val dotsByX = dots.groupBy({ it.first }, { it.second })
        val dotsByY = dots.groupBy({ it.second }, { it.first })

    }

    object : AoC(day = 13) {

        override fun treatPart(part: Int, answer: Number, day: Int) {
            if (part == 2) {
                print("Part 2 of day 13 does not fit in the framework.")
            } else {
                super.treatPart(part, answer, day)
            }
        }

        fun parse(input: List<String>): Pair<Paper, List<Pair<String, Int>>> {
            // Make a list of coordinates for the dots
            val dots = input
                .filter { it.trim() != "" && !it.startsWith("fold") }
                .flatMap {
                    val coords = it.split(',')
                    listOf(Pair(Integer.parseInt(coords[0]), Integer.parseInt(coords[1])))
                }

            // Also keep track of the folding instructions
            val instructions = input.filter { it.startsWith("fold") }
                .map { it.replace("fold along ", "").split('=') }
                .map { Pair(it[0], Integer.parseInt(it[1])) }

            return Pair(Paper(dots), instructions)
        }

        private fun fold(paper: Paper, instruction: Pair<String, Int>): Paper {
            val newDots = paper.dots.filter { isOnFoldingLine(instruction, it) }
                .mapNotNull { dot ->
                    if (instruction.first == "y") {
                        // Always copy over the dots before the folding line
                        if (dot.second < instruction.second) {
                            dot
                        } else {
                            val mirrorPoint = instruction.second + (instruction.second - dot.second)
                            // find mirror on the same line
                            if (paper.dotsByY[mirrorPoint]?.any { y -> y == dot.first } == true) null
                            else Pair(dot.first, mirrorPoint)
                        }
                    } else {
                        // Always copy over the dots before the folding line
                        if (dot.first < instruction.second) {
                            dot
                        } else {
                            val mirrorPoint = instruction.second + (instruction.second - dot.first)
                            // find mirror on the same line
                            if (paper.dotsByX[mirrorPoint]?.any { x -> x == dot.second } == true) null
                            else Pair(mirrorPoint, dot.second)
                        }
                    }
                }

            return Paper(newDots)
        }

        private fun isOnFoldingLine(
            instruction: Pair<String, Int>,
            it: Pair<Int, Int>
        ) = if (instruction.first == "x") {
            it.first != instruction.second
        } else {
            it.second != instruction.second
        }

        override fun part1(input: List<String>): Number {
            val (paper, instructions) = parse(input)

            val newPaper = fold(paper, instructions[0])

            return newPaper.dots.size
        }

        override fun check1ExpectedResult(): Number {
            return 17
        }

        override fun part2(input: List<String>): Number {
            var (paper, instructions) = parse(input)

            for (instruction in instructions) {
                paper = fold(paper, instruction)
            }

            printAsArray(paper)
            return 0
        }

        private fun printAsArray(paper: Paper) {
            println(paper)
            for (j in 0..paper.dotsByY.keys.maxOf { it }) {
                for (i in 0..paper.dotsByX.keys.maxOf { it }) {
                    val symbol = if (paper.dotsByX[i]?.find { it == j } == null) '.' else '#'
                    print(symbol)
                }
                println()
            }
        }

        override fun check2ExpectedResult(): Number {
            return 0
        }

    }.execute()

}
