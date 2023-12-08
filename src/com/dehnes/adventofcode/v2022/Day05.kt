package com.dehnes.adventofcode.v2022

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day05 {

    @Test
    fun run() {
        expectThat(moveCratesAndGetTop(true)) isEqualTo "ZWHVFWQWW"
        expectThat(moveCratesAndGetTop(false)) isEqualTo "HZFZCCWWV"
    }

    private fun moveCratesAndGetTop(reversed: Boolean): String {
        val (stacks, instructions) = parseInput()

        instructions.forEach { i ->
            val (remove, keep) = stacks[i.from - 1].splitAt(i.crates)
            stacks[i.from - 1] = keep
            stacks[i.to - 1] = remove.let { if (reversed) it.reversed() else it } + stacks[i.to - 1]
        }

        return stacks.mapNotNull { it.firstOrNull() }.joinToString(separator = "")
    }

    private fun parseInput(): Pair<MutableList<List<Char>>, List<Instruction>> {
        val instructions = mutableListOf<Instruction>()
        val stacks: MutableList<List<Char>> = mutableListOf()

        var readingStacks = true
        getLines().forEach { line ->
            if (line.startsWith("move")) {
                readingStacks = false
            }

            if (readingStacks) {
                var pos = 1
                var currentStack = 0
                while (pos < line.length) {
                    if (stacks.size <= currentStack) stacks.add(mutableListOf())
                    val crate = line[pos]
                    if (crate in 'A'..'Z') {
                        stacks[currentStack] = stacks[currentStack] + crate
                    }
                    pos += 4
                    currentStack++
                }
            } else {
                val parts = line.split(" ")
                instructions.add(Instruction(parts[1].toInt(), parts[3].toInt(), parts[5].toInt()))
            }
        }

        return stacks to instructions
    }

}

data class Instruction(
    val crates: Int,
    val from: Int,
    val to: Int
)

fun <T> List<T>.splitAt(index: Int): Pair<List<T>, List<T>> = this.subList(0, index) to this.subList(index, this.size)