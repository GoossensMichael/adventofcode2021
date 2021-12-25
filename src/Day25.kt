fun main() {

    object : AoC(day = 25) {
        override fun part1(input: List<String>): Number {
            fun isFree(input: Array<CharArray>, i: Int, j: Int) = input[i][j] == '.'

            val rows = input.size
            val cols = input[0].length

            var cucumbers = input.map { it.toCharArray() }.toTypedArray()
            print(cucumbers)

            var moved = true
            var count = 0
            while (moved) {
                count++
                moved = false
                val newCucumbers = Array(cucumbers.size) { CharArray(cucumbers[0].size) { '.' } }

                for (type in listOf('>', 'v')) {
                    for (i in 0 until newCucumbers.size) {
                        for (j in 0 until newCucumbers[i].size) {
                            if (type == cucumbers[i][j]) {
                                when (cucumbers[i][j]) {
                                    '>' -> {
                                        val col = (j + 1) % cols
                                        if (isFree(cucumbers, i, col)) {
                                            newCucumbers[i][col] = cucumbers[i][j]
                                            moved = true
                                        } else {
                                            newCucumbers[i][j] = cucumbers[i][j]
                                        }
                                    }
                                    'v' -> {
                                        val row = (i + 1) % rows
                                        if (isFree(newCucumbers, row, j) && cucumbers[row][j] != 'v') {
                                            newCucumbers[row][j] = cucumbers[i][j]
                                            moved = true
                                        } else {
                                            newCucumbers[i][j] = cucumbers[i][j]
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                println()
                println(count)
                print(newCucumbers)
                cucumbers = newCucumbers
            }

            return count
        }

        private fun print(cucumbers: Array<CharArray>) {
            for (row in cucumbers) {
                for (col in row) {
                    print(col)
                }
                println()
            }
        }

        override fun check1ExpectedResult(): Number {
            return 58
        }

        override fun part2(input: List<String>): Number {
            return 0
        }

        override fun check2ExpectedResult(): Number {
            return 0
        }

    }.execute()

}
