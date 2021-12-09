fun main() {

    object : AoC(day = 4) {
        private fun createBoards(input: List<String>): MutableList<Board> {
            val boards = mutableListOf<Board>()
            for (boardNumber in 0 until (input.size + 1) / 6) {
                val board = Board(Array(5) { IntArray(5) })
                for (row in 0 until 5) {
                    val inputRow = input[boardNumber * 6 + row]
                    board.numbers[row] = inputRow.split("[ ]+".toRegex()).stream()
                        .filter { it != "" }
                        .mapToInt { Integer.parseInt(it) }
                        .toArray()
                }
                boards.add(board)
            }

            return boards
        }

        override fun part1(input: List<String>): Number {
            val numbers = input[0].split(',').stream().mapToInt { Integer.parseInt(it) }.toArray()
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

        override fun check1ExpectedResult(): Number {
            return 4512
        }

        override fun part2(input: List<String>): Number {
            val numbers = input[0].split(',').stream().mapToInt { Integer.parseInt(it) }.toArray()
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

        override fun check2ExpectedResult(): Number {
            return 1924
        }

    }.execute()

}

class Board (val numbers: Array<IntArray>) {

    private val rowHits = Array(5) { mutableListOf<Int>() }
    private val colHits = Array(5) { mutableListOf<Int>() }

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
                            .toList()
                            .sum() * number
                    }
                }
            }
        }

        return null
    }
}
