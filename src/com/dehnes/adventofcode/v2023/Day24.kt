package com.dehnes.adventofcode.v2023

import com.dehnes.adventofcode.utils.Combinations.combinations
import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test

class Day24 {

    val stones = getLines().map { line ->
        val p = line.split(",").flatMap { it.split(" ") }.filterNot { it.isEmpty() }.map { it.trim() }
        val pos = listOf(p[0].toLong(), p[1].toLong(), p[2].toLong())
        val v = listOf(p[4].toLong(), p[5].toLong(), p[6].toLong())
        pos to v
    }

    @Test
    fun part1() {
        val testArea = 200000000000000L.toDouble()..400000000000000L.toDouble()
        val result = combinations(stones).count { (left, right) ->
            val (xy, times) = calcCross(right, left)
            val (x, y) = xy
            val (ta, tb) = times

            ta >= 1 && tb >= 1 && x in testArea && y in testArea
        }

        check(result == 20336)
    }

    @Test
    fun part2() {
        // credit: https://github.com/mdekaste/AdventOfCode2023/blob/master/src/main/kotlin/day24/Day24.kt

        val z3String = buildString {
            appendLine("(declare-const fx Int)")
            appendLine("(declare-const fy Int)")
            appendLine("(declare-const fz Int)")
            appendLine("(declare-const fdx Int)")
            appendLine("(declare-const fdy Int)")
            appendLine("(declare-const fdz Int)")
            stones.take(3).forEachIndexed{ i, (pos, vel) ->
                appendLine("(declare-const t$i Int)")
                appendLine("(assert (>= t$i 0))")
                appendLine("(assert (= (+ ${pos[0]} (* ${vel[0]} t$i)) (+ fx (* fdx t$i))))")
                appendLine("(assert (= (+ ${pos[1]} (* ${vel[1]} t$i)) (+ fy (* fdy t$i))))")
                appendLine("(assert (= (+ ${pos[2]} (* ${vel[2]} t$i)) (+ fz (* fdz t$i))))")
            }
            appendLine("(check-sat)")
            appendLine("(get-model)")
            appendLine("(eval (+ fx fy fz))")
        }
        println(z3String)

        // fy 343916889344399
        // fx 150345122760494
        // fz 183394034557877
        // = 677656046662770

    }

    private fun calcCross(
        a: Pair<List<Long>, List<Long>>,
        b: Pair<List<Long>, List<Long>>
    ): Pair<Pair<Double, Double>, Pair<Double, Double>> {
        val commonY = findCommonY(a, b)
        val commonX = findCommonX(a, b)

        val timeAx = xToTime(a, commonX)
        val timeBx = xToTime(b, commonX)

        return (commonX to commonY) to (timeAx to timeBx)
    }

    private fun findCommonY(a: Pair<List<Long>, List<Long>>, b: Pair<List<Long>, List<Long>>): Double {
        val (pa, va) = a
        val (pb, vb) = b
        val (pax, pay, paz) = pa
        val (vax, vay, vaz) = va
        val (pbx, pby, pbz) = pb
        val (vbx, vby, vbz) = vb

        val above = ((pay.toDouble() * vax) / vay) - ((pby.toDouble() * vbx) / vby) + pbx - pax
        val under = (vax.toDouble() / vay) - (vbx.toDouble() / vby)
        return above / under
    }

    private fun findCommonX(a: Pair<List<Long>, List<Long>>, b: Pair<List<Long>, List<Long>>): Double {
        val (pa, va) = a
        val (pb, vb) = b
        val (pax, pay, paz) = pa
        val (vax, vay, vaz) = va
        val (pbx, pby, pbz) = pb
        val (vbx, vby, vbz) = vb

        val above = ((pax.toDouble() * vay) / vax) - ((pbx.toDouble() * vby) / vbx) + pby - pay
        val under = (vay.toDouble() / vax) - (vby.toDouble() / vbx)
        return above / under
    }

    private fun xToTime(a: Pair<List<Long>, List<Long>>, x: Double) = (x - a.first[0]) / a.second[0]


}

