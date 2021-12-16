package com.dehnes.adventofcode.v2021

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class Day16 {

    val input = File("resources/2021/day16.txt").readLines().map {
        it.toList().joinToString("") {
            it.toString().let { Integer.parseInt(it, 16) }.let { Integer.toBinaryString(it).padStart(4, '0') }
        }
    }.first()

    @Test
    fun run() {
        val packet = parsePacket(0)

        assertEquals(949, countVersions(packet))
        assertEquals(1114600142730, calc(packet))
    }

    private fun calc(packet: Packet): Long = when (packet) {
        is LiteralPacket -> packet.value
        is OperatorPacket -> when (packet.type) {
            0 -> packet.subPackets.sumOf { calc(it) }
            1 -> packet.subPackets.fold(1L) { acc, p -> acc * calc(p) }
            2 -> packet.subPackets.minOf { calc(it) }
            3 -> packet.subPackets.maxOf { calc(it) }
            5 -> if (calc(packet.subPackets[0]) > calc(packet.subPackets[1])) 1 else 0
            6 -> if (calc(packet.subPackets[0]) < calc(packet.subPackets[1])) 1 else 0
            7 -> if (calc(packet.subPackets[0]) == calc(packet.subPackets[1])) 1 else 0
            else -> error("")
        }
    }

    private fun countVersions(packet: Packet): Long = when (packet) {
        is LiteralPacket -> packet.version.toLong()
        is OperatorPacket -> packet.version + packet.subPackets.sumOf { countVersions(it) }
    }

    private fun parsePacket(offset: Int): Packet {
        val version = input.substring(offset, offset + 3).toInt(2)
        val type = input.substring(offset + 3, offset + 6).toInt(2)
        return if (type == 4) {
            var pos = offset + 6
            var valueString = ""
            var parts = 0
            while (true) {
                parts++
                val s = input.substring(pos, pos + 5)
                valueString += s.substring(1)
                if (s.startsWith("0")) {
                    break
                }
                pos += 5
            }
            LiteralPacket(version, valueString.toLong(2), parts)
        } else {
            val lengthTypeId = input.substring(offset + 6, offset + 7).toInt(2)
            if (lengthTypeId == 0) {
                val lengthInBits = input.substring(offset + 7, offset + 7 + 15).toInt(2)
                val r = mutableListOf<Packet>()
                var pos = offset + 7 + 15
                while (pos < offset + 7 + 15 + lengthInBits) {
                    val packet = parsePacket(pos)
                    r.add(packet)
                    pos += packet.bits()
                }
                OperatorPacket(version, type, r, 15)
            } else {
                val subPackets = input.substring(offset + 7, offset + 7 + 11).toInt(2)
                val r = mutableListOf<Packet>()
                var pos = offset + 7 + 11
                while (r.size < subPackets) {
                    val packet = parsePacket(pos)
                    r.add(packet)
                    pos += packet.bits()
                }
                OperatorPacket(version, type, r, 11)
            }
        }
    }

}

sealed class Packet {
    abstract fun bits(): Int
}

data class LiteralPacket(
    val version: Int,
    val value: Long,
    val partCount: Int
) : Packet() {
    override fun bits() = (partCount * 5) + 3 + 3
}

data class OperatorPacket(
    val version: Int,
    val type: Int,
    val subPackets: List<Packet>,
    val extraBits: Int
) : Packet() {
    override fun bits() = 3 + 3 + 1 + extraBits + (subPackets.sumOf { it.bits() })
}