fun main() {

    data class Octopus(var level: Int,
                       var lastFlashStep: Int = 0,
                       val neighbours: MutableList<Octopus> = mutableListOf()) {

        fun addNeighbour(neighbour: Octopus) {
            neighbours.add(neighbour)
        }

        fun brighten(step: Int): Int {
            var flashes = 0
            if (lastFlashStep < step && level <= 9) {
                level++
                if (level > 9) {
                    level = 0
                    flashes++
                    lastFlashStep = step
                    neighbours.forEach { flashes += it.brighten(step) }
                }
            }

            return flashes
        }
    }

    object : AoC(day = 11) {

        override fun part1(input: List<String>): Number {
            val rows = input.size
            val cols = input[0].length

            val octopuses = Array(rows) { Array(cols) { Octopus(0) } }

            for (i in 0 until rows) {
                for (j in 0 until cols) {
                    octopuses[i][j].level = input[i][j].digitToInt()
                }
            }
            for (i in 0 until rows) {
                for (j in 0 until cols) {
                    for (x in -1..1) {
                        for (y in -1..1) {
                            val nx = i + x
                            val ny = j + y
                            if (nx in 0 until rows && ny in 0 until cols && !(nx == i && ny == j)) {
                                octopuses[i][j].addNeighbour(octopuses[nx][ny])
                            }
                        }
                    }
                }
            }

            var flashes = 0
            for (i in 1..100) {
                for (row in octopuses) {
                    for (octopus in row) {
                        flashes += octopus.brighten(i)
                    }
                }
            }

            return flashes
        }

        override fun check1ExpectedResult(): Number {
            return 1656
        }

        override fun part2(input: List<String>): Number {
            val rows = input.size
            val cols = input[0].length

            val octopuses = Array(rows) { Array(cols) { Octopus(0) } }

            for (i in 0 until rows) {
                for (j in 0 until cols) {
                    octopuses[i][j].level = input[i][j].digitToInt()
                }
            }
            for (i in 0 until rows) {
                for (j in 0 until cols) {
                    for (x in -1..1) {
                        for (y in -1..1) {
                            val nx = i + x
                            val ny = j + y
                            if (nx in 0 until rows && ny in 0 until cols && !(nx == i && ny == j)) {
                                octopuses[i][j].addNeighbour(octopuses[nx][ny])
                            }
                        }
                    }
                }
            }

            var flashes = 0
            var step = 0
            while (flashes < rows * cols) {
                step ++
                flashes = 0
                for (row in octopuses) {
                    for (octopus in row) {
                        flashes += octopus.brighten(step)
                    }
                }
            }

            return step
        }

        override fun check2ExpectedResult(): Number {
            return 195
        }

    }.execute()

}
