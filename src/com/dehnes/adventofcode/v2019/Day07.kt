package com.dehnes.adventofcode.v2019

import org.junit.jupiter.api.Test
import java.util.*

class Day07 {
    val originalCode = inputText(7).split(",").map { it.toLong() }

    @Test
    fun main() {
        var outputSignal = 0L
        combinations(listOf(0L, 1, 2, 3, 4), emptyList()) {
            var signal = 0L
            it.forEach { phase ->
                val l = LinkedList(listOf(phase, signal))
                intcodeComputer(originalCode, { l.removeFirst() }) { signal = it }
            }
            if (signal > outputSignal) {
                outputSignal = signal
            }
        }
        println(outputSignal) // 844468

        var outputSignal2 = 0L
        combinations(listOf(5L, 6, 7, 8, 9), emptyList()) { phasees ->
            var signal = 0L

            // calc output for phases
            var done = false
            val states = mutableMapOf<Long, State>()
            while (!done) {
                for (p in phasees) {
                    val result = if (states[p] == null) {
                        val s = State.init(originalCode.toMutableList())
                        states[p] = s
                        intcodeComputer_(s, p)
                    } else {
                        intcodeComputer_(states[p]!!, signal)
                    }

                    when (result) {
                        is Terminated -> done = true
                        is Output -> signal = result.value
                        else -> error("")
                    }
                }
            }

            if (signal > outputSignal2) {
                outputSignal2 = signal
            }

        }
        println(outputSignal2) // 4215746

    }

    fun combinations(input: List<Long>, current: List<Long>, fn: (List<Long>) -> Unit) {
        if (input.isNotEmpty()) {
            input.forEach { i ->
                combinations(input.filterNot { it == i }, current + i, fn)
            }
        } else {
            fn(current)
        }
    }
}