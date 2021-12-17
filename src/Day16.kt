import java.util.*

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
            if (literal != null) {
                return literal!!
            } else {
                return when (typeId.toInt(2)) {
                    0 -> packets.map { it.solve() }.sum()
                    1 -> packets.map { it.solve() }.fold(1L) { a, b -> a * b }
                    2 -> packets.minOf { it.solve() }
                    3 -> packets.maxOf { it.solve() }
                    5 -> if (packets[0].solve() > packets[1].solve()) 1 else 0
                    6 -> if (packets[0].solve() < packets[1].solve()) 1 else 0
                    7 -> if (packets[0].solve() == packets[1].solve()) 1 else 0
                    else -> error("Type id $typeId can not be solved")
                }
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
                    while (packet.packets.size < packet.length!!) {
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
            return 16
        }

        override fun part2(input: List<String>): Number {
            return processPackets(input.parse()).solve()
        }

        override fun check2ExpectedResult(): Number {
            return 15L
        }

        private fun List<String>.parse(): String {
            return this[0].map { hexMap[it]!! }.joinToString("")
        }

    }.execute()

}
