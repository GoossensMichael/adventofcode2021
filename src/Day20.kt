fun main() {

    object : AoC(day = 20) {

        override fun part1(input: List<String>): Number {
            return solve(input, 2)
        }

        override fun check1ExpectedResult(): Number {
            return 35
        }

        override fun part2(input: List<String>): Number {
            return solve(input, 50)
        }

        override fun check2ExpectedResult(): Number {
            return 3351
        }

        private fun List<String>.parse() = Pair(this[0], this.subList(2, this.size))


        private fun solve(input: List<String>, repeat: Int): Int {
            val (algorithm, inputImage) = input.parse()

            var infinitePlaneState = false
            var result = inputImage
            repeat(repeat) {
                result = enhance(algorithm, result, infinitePlaneState)

                // determine state of the infinite plane (light = true, dark = false)
                infinitePlaneState = infinitePlaneSwitch(algorithm, infinitePlaneState)
            }

            return result.sumOf { line -> line.count { pixel -> pixel == '#' } }
        }

        private fun infinitePlaneSwitch(algorithm: String, currentInfinitePlaneState: Boolean): Boolean {
            // The infinite plane on blinks when the first item in the algorithm is # and the last item is .
            // The infinite plane stays dark if the first item in the algorithm is .
            // The infinite plane turns light after the first round and stays that way if the first and last item in the algorithm is #
            return when {
                algorithm[0] == '.' -> false
                algorithm[0] == '#' && algorithm[algorithm.length - 1] == '.' -> !currentInfinitePlaneState
                algorithm[0] == '#' && algorithm[algorithm.length - 1] == '#' -> true
                else -> error("All cases are covered, this error should never show (ae3ef3)")
            }
        }


        private fun enhance(algorithm: String, image: List<String>, infinitePlaneState: Boolean): List<String> {
            fun isWithinBounds(x: Int, y: Int) =
                x >= 0 && x < image[0].length && y >= 0 && y < image.size

            fun encode(i: Int, j: Int): Char {
                var mask = ""
                for (x in i - 1..i + 1) {
                    for (y in j - 1..j + 1) {
                        mask += if (isWithinBounds(x, y) && image[x][y] == '#') 1 else {
                            if (!isWithinBounds(x, y) && infinitePlaneState) 1 else 0
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
