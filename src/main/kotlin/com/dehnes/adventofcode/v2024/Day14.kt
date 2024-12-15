package com.dehnes.adventofcode.v2024

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import com.dehnes.adventofcode.utils.PointInt
import org.junit.jupiter.api.Test
import java.awt.Color
import java.awt.image.BufferedImage

class Day14 {
    val robots = getLines().map {
        val (l, r) = it.split(" ")
        Robot(
            l.drop(2).split(",").let { PointInt(it[0].toInt(), it[1].toInt()) },
            r.drop(2).split(",").let { PointInt(it[0].toInt(), it[1].toInt()) },
        )
    }

    val area = PointInt(101, 103)

    @Test
    fun run() {
        // move 100 seconds
        val moved = robots.map { move(it, 100) }

        // split into 4 quadrants
        val q = Array<Long>(4) { 0 }
        val middelX = (area.x - 1) / 2
        val middelY = (area.y - 1) / 2
        moved.forEach {
            when {
                it.x in 0..<middelX && it.y in 0..<middelY -> q[0]++
                it.x in (middelX + 1)..<area.x && it.y in 0..<middelY -> q[1]++
                it.x in 0..<middelX && it.y in (middelY + 1)..area.y -> q[2]++
                it.x in (middelX + 1)..<area.x && it.y in (middelY + 1)..area.y -> q[3]++
            }
        }

        // part1
        check(232589280L == q.reduce { a, b -> a * b })


        // find the max seconds at which the pattern repeats
        /*
        val r = mutableMapOf<String, Int>()
        repeat(100000) { time ->
            val moved = robots.map { move(it, time) }
            val sha1 = sha1(moved.toString().toByteArray())
            if (sha1 in r) {
                println(time) // 10403 seconds!!
                System.exit(0)
            }
            r[sha1] = time
        }
        */

        // create an image which fits all patterns
        val imgArea = PointInt(105, 105)
        val img = BufferedImage(imgArea.x * area.x, imgArea.y * area.y, BufferedImage.TYPE_INT_RGB)
        var timeInSeconds = 1
        repeat(imgArea.y) { yOffset ->
            repeat(imgArea.x) { xOffset ->

                // draw separation lines
                (0..<area.x).forEach { x ->
                    img.setRGB((xOffset * area.x) + x, (yOffset * area.y), Color.WHITE.rgb)
                }
                (0..<area.y).forEach { y ->
                    img.setRGB((xOffset * area.x), (yOffset * area.y) + y, Color.WHITE.rgb)
                }

                robots.map { move(it, timeInSeconds) }.forEach {
                    img.setRGB((xOffset * area.x) + it.x, (yOffset * area.y) + it.y, Color.WHITE.rgb)
                }

                timeInSeconds++
            }
        }
        // print it
        //ImageIO.write(img, "png", File("day14.png"))

        // look at the image and find the x-mas tree - at sub-image: Y:73 X:9
        val part2 = 72 * imgArea.y + 9
        check(7569 == part2)
        printMap(robots.map { move(it, part2) })
    }

    private fun printMap(r: List<PointInt>) {
        (0..<area.y).forEach { y ->
            (0..<area.x).forEach { x ->
                val pos = PointInt(x, y)
                if (r.any { it == pos })
                    print('#')
                else
                    print(' ')
            }
            println()
        }
        println()
    }

    private fun move(r: Robot, times: Int) = PointInt(
        x = ((r.pos.x + (r.velocity.x * times)) % area.x).let {
            if (it < 0) it + area.x else it
        },
        y = ((r.pos.y + (r.velocity.y * times)) % area.y).let {
            if (it < 0) it + area.y else it
        }
    )

    data class Robot(val pos: PointInt, val velocity: PointInt)
}