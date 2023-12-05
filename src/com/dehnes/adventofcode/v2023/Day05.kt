package com.dehnes.adventofcode.v2023

import com.dehnes.adventofcode.utils.RangeUtils.combine
import com.dehnes.adventofcode.utils.RangeUtils.offset
import com.dehnes.adventofcode.utils.RangeUtils.overlaps
import org.junit.jupiter.api.Test
import java.io.File
import java.nio.charset.StandardCharsets
import java.util.*

class Day05 {

    val data = File("resources/2023/day05.txt").readText(StandardCharsets.UTF_8)
    val sections = data.split("\n\n")
    val seedNumbers = sections[0].split(": ")[1].split(" ").map { it.trim().toLong() }
    val mappings = sections.drop(1).map { parseMappings(it) }

    @Test
    fun run() {
        val part1 = seedNumbers.map { MappingRange(it..it, 0) }
        check(lowestLocation(part1) == 510109797L)

        val part2 = seedNumbers
            .windowed(2, 2)
            .map { (it[0]..<(it[0] + it[1])) }
            .map { MappingRange(it, 0) }
        check(lowestLocation(part2) == 9622622L)
    }

    private fun lowestLocation(ranges: List<MappingRange>): Long = ranges
        .map {
            optimize(it, mappings)
                .map { it.range to it.range.offset(it.delta) }
                .minBy { it.second.first }
        }
        .minBy { it.second.first }
        .second
        .first

    private fun optimize(input: MappingRange, mappings: List<List<MappingRange>>): List<MappingRange> {
        if (mappings.isEmpty()) {
            return listOf(input)
        }

        val nextMapping = mappings.first()
        val remainingMapping = mappings.drop(1)

        val compiled = input.range.offset(input.delta)
        val matches = nextMapping.filter { it.range.overlaps(compiled) }

        val matched = matches.map { m ->
            val c = compiled.combine(m.range).single { it.isCommon }.r
            MappingRange(
                c.offset(input.delta * -1),
                m.delta + input.delta
            )
        }

        return matched.flatMap { optimize(it, remainingMapping) }
    }


    private fun parseMappings(str: String): List<MappingRange> {
        val result = LinkedList<MappingRange>()
        result.add(
            MappingRange(
                Long.MIN_VALUE..Long.MAX_VALUE,
                0
            )
        )

        str.split("\n").drop(1).forEach {
            val numbers = it.split(" ").map { it.trim().toLong() }

            val range = MappingRange(
                numbers[1]..<(numbers[1] + numbers[2]),
                numbers[0] - numbers[1],
            )

            val toBeSplittedIndex = result.indexOfFirst { it.range.overlaps(range.range) }
            val toBeSplitted = result.get(toBeSplittedIndex)

            val split = toBeSplitted.range
                .combine(range.range).map {
                    if (it.isCommon) {
                        MappingRange(
                            it.r,
                            toBeSplitted.delta + range.delta
                        )
                    } else {
                        MappingRange(
                            it.r,
                            toBeSplitted.delta
                        )
                    }
                }

            split.reversed().forEach { result.add(toBeSplittedIndex, it) }
            result.removeAt(toBeSplittedIndex + split.size)
        }

        return result
    }

    data class MappingRange(
        val range: LongRange,
        val delta: Long,
    )

}