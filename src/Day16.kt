fun main() {

    val hexMap = mapOf(
        Pair('0', "0000"),
        Pair('1', "0001"),
        Pair('2', "0010"),
        Pair('3', "0011"),
        Pair('4', "0100"),
        Pair('5', "0101"),
        Pair('6', "0110"),
        Pair('7', "0111"),
        Pair('8', "1000"),
        Pair('9', "1001"),
        Pair('A', "1010"),
        Pair('B', "1011"),
        Pair('C', "1100"),
        Pair('D', "1101"),
        Pair('E', "1110"),
        Pair('F', "1111")
    )

    data class Packet(
        val version: Int,
        val typeId: String,
        var lengthTypeId: Int? = null,
        var length: Int? = null,
        var literal: Long? = null,
        val packets: MutableList<Packet> = mutableListOf(),
        var bits: Int? = null
    ) {
        fun versionSum(): Int {
            return version + packets.sumOf { it.versionSum() }
        }

        fun solve(): Long {
            return when (typeId.toInt(2)) {
                0 -> packets.sumOf { it.solve() }
                1 -> packets.map { it.solve() }.reduce { a, b -> a * b }
                2 -> packets.minOf { it.solve() }
                3 -> packets.maxOf { it.solve() }
                4 -> literal!!
                5 -> if (packets[0].solve() > packets[1].solve()) 1 else 0
                6 -> if (packets[0].solve() < packets[1].solve()) 1 else 0
                7 -> if (packets[0].solve() == packets[1].solve()) 1 else 0
                else -> error("Type id $typeId can not be solved")
            }
        }
    }

    object : AoC(day = 16) {

        fun processLiteral(data: String): Pair<Long, Int> {
            var literal = ""

            var position = 0
            var loop = true
            while (loop) {
                literal += data.substring(position + 1..position + 4)
                loop = data[position] == '1'
                position += 5
            }

            return Pair(literal.toLong(2), position)
        }

        fun processPackets(data: String): Packet {
            val packet = Packet(data.substring(0..2).toInt(2), data.substring(3..5))

            if (packet.typeId == "100") {
                val (literal, bitcount) = processLiteral(data.substring(6))
                packet.literal = literal
                packet.bits = 6 + bitcount
                return packet
            } else {
                packet.lengthTypeId = data.substring(6..6).toInt(2)
                if (packet.lengthTypeId == 0) {
                    packet.length = data.substring(7..21).toInt(2)

                    var bitsRead = 0
                    while (bitsRead < packet.length!!) {
                        val result = processPackets(data.substring(22 + bitsRead))
                        bitsRead += result.bits!!
                        packet.packets.add(result)
                    }

                    packet.bits = 22 + bitsRead
                    return packet
                } else {
                    packet.length = data.substring(7..17).toInt(2)

                    var bitsRead = 0
                    repeat(packet.length!!) {
                        val result = processPackets(data.substring(18 + bitsRead))
                        bitsRead += result.bits!!
                        packet.packets.add(result)
                    }

                    packet.bits = 18 + bitsRead
                    return packet
                }
            }
        }

        override fun part1(input: List<String>): Number {
            return processPackets(input.parse()).versionSum()
        }

        override fun check1ExpectedResult(): Number {
            /*
             * test = 16
             * test 2 = 12
             * test 3 = 23
             * test 4 = 31
             */
            return 16
        }

        override fun part2(input: List<String>): Number {
            return processPackets(input.parse()).solve()
        }

        /*
         * test = 15L
         * test 2 = ?
         * test 3 = ?
         * test 4 = ?
         *
         * Extra test data:
         * C200B40A82 finds the sum of 1 and 2, resulting in the value 3.
         * 04005AC33890 finds the product of 6 and 9, resulting in the value 54.
         * 880086C3E88112 finds the minimum of 7, 8, and 9, resulting in the value 7.
         * CE00C43D881120 finds the maximum of 7, 8, and 9, resulting in the value 9.
         * D8005AC2A8F0 produces 1, because 5 is less than 15.
         * F600BC2D8F produces 0, because 5 is not greater than 15.
         * 9C005AC2F8F0 produces 0, because 5 is not equal to 15.
         * 9C0141080250320F1802104A08 produces 1, because 1 + 3 = 2 * 2.
         */
        override fun check2ExpectedResult(): Number {
            return 15L
        }

        private fun List<String>.parse(): String {
            return this[0].map { hexMap[it]!! }.joinToString("")
        }

    }.execute()

}
