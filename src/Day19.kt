import kotlin.math.abs

typealias Location = Triple<Int, Int, Int>
typealias Beacon = Triple<Int, Int, Int>
typealias Vector3D = Triple<Int, Int, Int>

fun main() {

    data class Scanner(val id: Int, val beacons: Set<Beacon>, var realLocation: Location? = null)

    object : AoC(day = 19) {

        val MIN_BEACON_OVERLAP = 12

        override fun part1(input: List<String>): Number {
            return solve(input).first.size
        }

        override fun check1ExpectedResult(): Number {
            return 79
        }

        override fun part2(input: List<String>): Number {
            fun manhattan(a: Location, b: Location) =
                abs(a.first - b.first) + abs(a.second - b.second) + abs(a.third - b.third)

            val (_, scanners) = solve(input)

            val scannerList = scanners.toList()
            var maxManhattan = 0
            for ((i, first) in scannerList.withIndex()) {
                for (j in i + 1 until scannerList.size) {
                    val second = scannerList[j]
                    val manhattan = manhattan(first.realLocation!!, second.realLocation!!)
                    if (manhattan > maxManhattan) {
                        maxManhattan = manhattan
                    }
                }
            }

            return maxManhattan
        }

        override fun check2ExpectedResult(): Number {
            return 3621
        }

        private fun List<String>.parse(): List<Scanner> {
            fun getScannerId(line: String) = line.substring("--- scanner ".length, line.length - " ---".length).toInt()

            val scanners = mutableSetOf<Scanner>()

            var i = 0
            var line = this[i]
            var scannerId = getScannerId(line)

            var beacons = mutableSetOf<Beacon>()
            i++
            while (i < this.size) {
                line = this[i]
                when {
                    line.startsWith("--- scanner") -> {
                        scannerId = getScannerId(line)
                        beacons = mutableSetOf()
                    }
                    line.isBlank() -> {
                        scanners.add(Scanner(scannerId, beacons))
                    }
                    else -> {
                        val coords = line.split(',').map { it.toInt() }
                        beacons.add(Beacon(coords[0], coords[1], coords[2]))
                    }
                }
                i++
            }
            scanners.add(Scanner(scannerId, beacons))

            return scanners.toList()
        }

        private fun solve(input: List<String>): Pair<MutableSet<Beacon>, MutableSet<Scanner>> {
            val scanners = input.parse().toMutableSet()

            // # INIT PHASE

            // Get scanner 0. It is considered to be at location (0, 0, 0) with orientation (0, 0, 1)
            val scanner0 = scanners.find { it.id == 0 } ?: error("There should be always one scanner 0")

            // The solution includes all beacons from the viewpoint of scanner0
            // It can therefore be initialized with the beacons of scanner0
            val solution = mutableSetOf(*scanner0.beacons.toTypedArray())

            // By giving a scanner a location we also mark it as located and all its beacons to be registered
            scanner0.realLocation = Location(0, 0, 0)
            val solvedScanners = mutableSetOf(scanner0)

            // For all the other scanners try to find at least 12 overlapping beacons
            var unsolvedScanners = scanners.minus(solvedScanners)
            var solvedScannersToMatch = setOf(scanner0)
            var infiniteLoopDetected = false
            while (unsolvedScanners.isNotEmpty() && !infiniteLoopDetected) {
                // For all new solved scanners (initially just scanner 0) try to transform the origin and find at least
                // 12 matches.
                val newlySolvedScanners = mutableSetOf<Scanner>()
                for (solvedScanner in solvedScannersToMatch) {
                    for (unsolvedScanner in unsolvedScanners) {
                        var matchesFound = false

                        var unsolvedScannerBeaconsList = unsolvedScanner.beacons.toList()

                        var x = 0
                        while (!matchesFound && x < 4) {
                            var y = 0
                            while (!matchesFound && y < 4) {
                                var z = 0
                                while (!matchesFound && z < 4) {
                                    // No need to process the 11 last beacons as no match can be found anymore with 12 beacons
                                    for (unsolvedScannerBeacon in unsolvedScannerBeaconsList.subList(
                                        0,
                                        unsolvedScannerBeaconsList.size - MIN_BEACON_OVERLAP + 1
                                    )) {

                                        for (solvedBeacon in solvedScanner.beacons.toList().subList(0, solvedScanner.beacons.size - MIN_BEACON_OVERLAP + 1)) {
                                            val moveSolvedScannerOperation = Vector3D(
                                                unsolvedScannerBeacon.first - solvedBeacon.first,
                                                unsolvedScannerBeacon.second - solvedBeacon.second,
                                                unsolvedScannerBeacon.third - solvedBeacon.third
                                            )

                                            val solvedScannerWithChangedOrigin =
                                                changeOrigin(solvedScanner, moveSolvedScannerOperation)
                                            val matches =
                                                solvedScannerWithChangedOrigin.beacons.intersect(unsolvedScannerBeaconsList.toSet())
                                            if (matches.size >= MIN_BEACON_OVERLAP) {
                                                unsolvedScanner.realLocation = Location(
                                                    -moveSolvedScannerOperation.first,
                                                    -moveSolvedScannerOperation.second,
                                                    -moveSolvedScannerOperation.third
                                                )

                                                val newlySolvedScannerBeaconsList = unsolvedScannerBeaconsList.map { b ->
                                                    Beacon(
                                                        b.first + unsolvedScanner.realLocation!!.first,
                                                        b.second + unsolvedScanner.realLocation!!.second,
                                                        b.third + unsolvedScanner.realLocation!!.third
                                                    )
                                                }.toSet()
                                                solution.addAll(newlySolvedScannerBeaconsList)

                                                val newlySolvedScanner = Scanner(unsolvedScanner.id, newlySolvedScannerBeaconsList, unsolvedScanner.realLocation)
                                                scanners.removeIf { it.id == newlySolvedScanner.id }
                                                scanners.add(newlySolvedScanner)
                                                solvedScanners.add(newlySolvedScanner)
                                                newlySolvedScanners.add(newlySolvedScanner)
                                                matchesFound = true
                                                break
                                            }
                                        }
                                        if (matchesFound) {
                                            break
                                        }
                                    }
                                    if (!matchesFound) {
                                        z++

                                        unsolvedScannerBeaconsList = unsolvedScannerBeaconsList.map { rotateZ90(it) }
                                    }
                                }
                                if (!matchesFound) {
                                    y++

                                    unsolvedScannerBeaconsList = unsolvedScannerBeaconsList.map { b -> rotateY90(b) }
                                }
                            }
                            if (!matchesFound) {
                                x++

                                unsolvedScannerBeaconsList = unsolvedScannerBeaconsList.map { b -> rotateX90(b) }
                            }
                        }
                    }
                }

                solvedScannersToMatch = newlySolvedScanners.toSet()

                // Only keep checking when there are at least 12 beacons left to try and find a match

                unsolvedScanners = scanners.minus(solvedScanners)
                infiniteLoopDetected = newlySolvedScanners.isEmpty()
            }

            if (infiniteLoopDetected) {
                println("An infinite loop was detected! Could not solve for all scanners")
            }

            return Pair(solution, solvedScanners)
        }

        private fun changeOrigin(scanner: Scanner, operation: Vector3D): Scanner {
            return Scanner(scanner.id, scanner.beacons.map {
                Beacon(it.first + operation.first, it.second + operation.second, it.third + operation.third)
            }.toSet())
        }

    }.execute()

}
