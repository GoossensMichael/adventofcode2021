fun main() {

    object : AoC(day = 20) {

        override fun part1(input: List<String>): Number {
            println("P1")
            return solve(input, 2)
        }

        override fun check1ExpectedResult(): Number {
            return 35
        }

        override fun part2(input: List<String>): Number {
            println("P2")
            return solve(input, 50)
        }

        override fun check2ExpectedResult(): Number {
            return 3351
        }

        private fun List<String>.parse() = Pair(this[0], this.subList(2, this.size))

        private fun solve(input: List<String>, repeat: Int): Int {
            val (algorithm, inputImage) = input.parse()

            var result = inputImage
            repeat(repeat) {
                result = enhance(algorithm, result)
                result.forEach { println(it) }
                println()
            }

            return result.sumOf { line -> line.count { pixel -> pixel == '#' } }
        }

        private fun enhance(algorithm: String, image: List<String>): List<String> {
            fun isWithinBounds(x: Int, y: Int) =
                x >= 0 && x < image[0].length && y >= 0 && y < image.size

            fun encode(i: Int, j: Int): Char {
                var mask = ""
                for (x in i-1..i+1) {
                    for (y in j-1..j+1) {
                        if (isWithinBounds(x, y)) {
                            mask += if (image[x][y] == '#') 1 else 0
                        } else {
                            mask += 0
                        }
                    }
                }

                return algorithm[mask.toInt(2)]
            }

            val outputImage = mutableListOf<String>()

            for (i in -1..image.size) {
                var outputLine = ""
                for (j in -1..image.size) {
                    outputLine += encode(i, j)
                }
                outputImage.add(outputLine)
            }

            return outputImage
        }

    }.execute()

}
