package com.dehnes.adventofcode.v2023

import com.dehnes.adventofcode.utils.Dijkstra
import com.dehnes.adventofcode.utils.Direction
import com.dehnes.adventofcode.utils.Direction.Companion.down
import com.dehnes.adventofcode.utils.Direction.Companion.left
import com.dehnes.adventofcode.utils.Direction.Companion.right
import com.dehnes.adventofcode.utils.Direction.Companion.up
import com.dehnes.adventofcode.utils.ParserUtils.getLines
import com.dehnes.adventofcode.utils.PointInt
import com.dehnes.adventofcode.utils.PointInt.Companion.create
import com.dehnes.adventofcode.utils.plus
import org.junit.jupiter.api.Test

class Day17 {

    val lines = getLines().map { it.toList().map { it.toString().toInt() }.toIntArray() }.toTypedArray()
    val start = create(0 to 0)
    val end = PointInt(lines[0].size - 1, lines.size - 1)

    // credit: https://github.com/maksverver/AdventOfCode/blob/master/2023/17-slower.py
    @Test
    fun run() {
        check(solve(0, 3) == 1013L)
        check(solve(4, 10) == 1215L)
    }

    private fun solve(minRepeat: Int, maxRepeat: Int) = Dijkstra.solve(Node(start, null, 0)) { n ->
        listOf(right, down, left, up)
            .filterNot { it.reverseDirection() == n.d }
            .mapNotNull { candidateDir ->
                if (n.steps != 0 && if (n.d != candidateDir) n.steps < minRepeat else n.steps >= maxRepeat) return@mapNotNull null

                val pos = create(n.p.toPoint() + candidateDir.toPair())
                val distance = lines.getOrNull(pos.y)?.getOrNull(pos.x) ?: return@mapNotNull null
                Node(
                    pos,
                    candidateDir,
                    if (n.d == candidateDir) n.steps + 1 else 1
                ) to distance.toLong()
            }
    }
        .filter { it.key.p == end && it.key.steps >= minRepeat }
        .minBy { it.value }
        .value

    data class Node(val p: PointInt, val d: Direction?, val steps: Int)
}