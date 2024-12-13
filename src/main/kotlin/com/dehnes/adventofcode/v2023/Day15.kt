package com.dehnes.adventofcode.v2023

import com.dehnes.adventofcode.utils.ParserUtils.getText
import org.junit.jupiter.api.Test
import java.util.*

class Day15 {

    @Test
    fun run() {
        val input = getText().trim()

        val part1 = input.split(",").sumOf { hash(it) }
        check(part1 == 513172)

        val boxes = Array(256) { LinkedList<Lens>() }
        input.split(",")
            .map {
                if (it.contains("=")) {
                    AddLens(Lens(it.split("=")[0], it.split("=")[1].toInt()))
                } else {
                    RemoveLens(it.dropLast(1))
                }
            }
            .forEach {ins ->
                val box = boxes[hash(ins.label)]
                val index = box.indexOfFirst { it.label == ins.label }
                when (ins) {
                    is AddLens -> {
                        if (index != -1) {
                            box.add(index, ins.l)
                            box.removeAt(index + 1)
                        } else {
                            box.addLast(ins.l)
                        }
                    }

                    is RemoveLens -> {
                        if (index != -1) {
                            box.removeAt(index)
                        }
                    }
                }
            }

        val part2 = boxes.mapIndexed { i, box ->
            box.mapIndexed { i2, lens -> listOf(i + 1, i2 + 1, lens.fl).reduce { acc, i -> acc * i } }.sum()
        }.sum()

        check(part2 == 237806)
    }

    private fun hash(input: String) = input.toList().fold(0) { acc, c -> ((acc + c.code) * 17) % 256 }

    data class Lens(val label: String, val fl: Int)

    sealed class Instruction {
        abstract val label: String
    }

    data class AddLens(val l: Lens) : Instruction() {
        override val label: String
            get() = l.label
    }

    data class RemoveLens(override val label: String) : Instruction()

}

