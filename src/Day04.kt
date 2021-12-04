import kotlin.streams.toList

class Board (val numbers: Array<IntArray>) {

    val rowHits = Array(5) { mutableListOf<Int>() }
    val colHits = Array(5) { mutableListOf<Int>() }

    fun verifyNumber(number: Int): Int? {
        for (i in numbers.indices) {
            for (j in 0 until numbers[i].size) {
                if (numbers[i][j] == number) {
                    rowHits[i].add(number)
                    colHits[j].add(number)

                    numbers[i][j] = -1

                    if (rowHits[i].size == 5 || colHits[j].size == 5) {
                        return numbers.toList().stream()
                            .map { it.toList().stream().filter { n -> n >= 0 }.toList().sum() }
                            .peek { println(it) }
                            .toList()
                            .sum() * number
                    }
                }
            }
        }

        return null
    }
}

fun main() {

    val day = 4;

    fun createBoards(input: List<String>): MutableList<Board> {
        val boards = mutableListOf<Board>()
        for (boardNumber in 0 until (input.size + 1) / 6) {
            val board = Board(Array(5) { IntArray(5) })
            for (row in 0 until 5) {
                val inputRow = input.get(boardNumber * 6 + row)
                board.numbers[row] = inputRow.split("[ ]+".toRegex()).stream()
                    .filter { it != "" }
                    .mapToInt { Integer.parseInt(it) }
                    .toArray()
            }
            boards.add(board)
        }

        return boards
    }

    fun part1(input: List<String>): Int {
        val numbers = input.get(0).split(',').stream().mapToInt({ Integer.parseInt(it) }).toArray()
        val boards = createBoards(input.subList(2, input.size))

        for (number in numbers) {
            for (board in boards) {
                val bingo = board.verifyNumber(number)
                if (bingo != null) {
                    return bingo
                }
            }
        }

        return 0
    }

    fun part2(input: List<String>): Int {
        val numbers = input.get(0).split(',').stream().mapToInt({ Integer.parseInt(it) }).toArray()
        val boards = createBoards(input.subList(2, input.size))

        for (number in numbers) {
            val boardsToUse = boards.toList()
            for (board in boardsToUse) {
                val bingo = board.verifyNumber(number)
                if (bingo != null) {
                    boards.remove(board)
                    if (boards.size == 0) {
                        return bingo
                    }
                }
            }
        }

        return 0
    }

    fun treatPart(part: Int, answer: Int) {
        print("Submit part $part result $answer? (y|n): ")
        if (readLine() == "y") {
            sendAnswer(day, part, answer)
        }
    }

    // test if implementation meets criteria from the description for part 1, like:
    val testInput = readInput(String.format("Day%02d_test", day))
    check(part1(testInput) == 4512)

    // test was ok retrieve the real data
    val input = readInput(String.format("Day%02d", day))

    // get the answer with the real data for part 1
    treatPart(1, part1(input))

    // test if implementation meets criteria from the description for part 2, like:
    check(part2(testInput) == 1924)

    // get the answer with the real data for part 2
    treatPart(2, part2(input))
}
