package com.dehnes.adventofcode_2020.day13

val input = "29,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,41,x,x,x,x,x,x,x,x,x,521,x," +
        "x,x,x,x,x,x,23,x,x,x,x,13,x,x,x,17,x,x,x,x,x,x,x,x,x,x,x,x,x,601,x,x,x,x,x," +
        "37,x,x,x,x,x,x,x,x,x,x,x,x,19"

fun main() {
    val timestamp = 1002461
    val (buss, timeToWait) = input.split(",").filterNot { it == "x" }
            .map { it.toInt() }
            .map { b -> b to b - (timestamp % b) }.minByOrNull { it.second }!!
    println("Part1: ${buss * timeToWait}") // 4207

    val busses = input.split(",")
            .mapIndexed { index, s ->
                (if (s == "x") -1 else s.toLong()) to index.toLong()
            }
            .filter { it.first >= 0 }
            .fold(1L to 0L) { acc, b ->
                lcd(acc.first, b.first) to alignOffset(acc.second, acc.first, b.first, b.second)
            }

    println("Part2: ${busses.second}") // 725850285300475
}

fun alignOffset(initOffset: Long, n1: Long, n2: Long, n2Delta: Long): Long {
    var rounds = 0
    var time: Long
    while (true) {
        time = rounds * n1 + initOffset
        if ((time + n2Delta) % n2 == 0L) {
            break
        }
        rounds++
    }
    return time
}

fun lcd(n1: Long, n2: Long): Long = (n1 * n2) / hcf(n1, n2)
fun hcf(n1: Long, n2: Long): Long = if (n2 != 0L) hcf(n2, n1 % n2) else n1