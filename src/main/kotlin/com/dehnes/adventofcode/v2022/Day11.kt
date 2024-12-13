package com.dehnes.adventofcode.v2022

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test
import java.util.*

class Day11 {

    @Test
    fun run() {
        check(calculate(parse(), 3, 20) == 50830L)
        check(calculate(parse(), 1, 10000) == 14399640002L)
    }

    private fun calculate(monkes: List<Monkey>, div: Long, rounds: Int): Long {
        val mod = monkes.fold(1) { acc, monkey -> acc * monkey.test }
        repeat(rounds) {
            monkes.forEach { monkey ->
                while (monkey.items.isNotEmpty()) {
                    monkey.inspected++
                    val old = monkey.items.removeFirst()
                    val new = monkey.opp(old).mod(mod) / div

                    val test = new.mod(monkey.test) == 0
                    val to = if (test) monkey.testTrue else monkey.testFalse
                    monkes[to].items.addLast(new)
                }
            }
        }

        val sorted = monkes.toList().sortedBy { it.inspected }.reversed()
        return sorted[0].inspected * sorted[1].inspected
    }


    private fun parse(): MutableList<Monkey> {
        val monkes = mutableListOf<Monkey>()
        var currentMonkey = Monkey(LinkedList(), { it }, 1, 0, 0)
        getLines().forEach { line ->
            if (line.isEmpty()) {
                monkes.add(currentMonkey)
                currentMonkey = Monkey(LinkedList(), { it }, 1, 0, 0)
            } else if (line.contains("Starting")) {
                currentMonkey.items = LinkedList(
                    line.split(":")[1].split(",").map { it.trim().toLong() }
                )
            } else if (line.contains("Operation")) {
                val op = line.split(" ")[6]
                val numberStr = line.split(" ")[7]
                when {
                    numberStr == "old" && op == "*" -> currentMonkey.opp = { it * it }
                    numberStr != "old" && op == "*" -> currentMonkey.opp = { it * numberStr.toInt() }
                    numberStr != "old" && op == "+" -> currentMonkey.opp = { it + numberStr.toInt() }
                    else -> error("")
                }
            } else if (line.contains("Test")) {
                currentMonkey.test = line.split(" ").last().toInt()
            } else if (line.contains("true")) {
                currentMonkey.testTrue = line.split(" ").last().toInt()
            } else if (line.contains("false")) {
                currentMonkey.testFalse = line.split(" ").last().toInt()
            }
        }
        monkes.add(currentMonkey)
        return monkes
    }

}

data class Monkey(
    var items: LinkedList<Long>,
    var opp: (item: Long) -> Long,
    var test: Int,
    var testTrue: Int,
    var testFalse: Int,
    var inspected: Long = 0L
)

