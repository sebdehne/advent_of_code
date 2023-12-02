package com.dehnes.adventofcode.v2023

import org.junit.jupiter.api.Test
import java.io.File

class Day02 {

    val games = File("resources/2023/day02.txt").readLines().map { line ->
        val (game, d) = line.split(":")

        Game(
            game.replace("Game ", ""),
            d.split(";").map { g ->
                val colors = g.trim().split(",").map { it.trim() }.map {
                    val (amount, color) = it.split(" ")
                    color to amount.toLong()
                }.toMap()

                Drawn(
                    colors["red"] ?: 0L,
                    colors["green"] ?: 0,
                    colors["blue"] ?: 0,
                )
            }
        )
    }

    @Test
    fun part1() {
        val target = Drawn(12, 13, 14)

        val pssible = games.filter { g ->
            g.drawn.all { d ->
                d.red <= target.red && d.green <= target.green && d.blue <= target.blue
            }
        }.sumOf { it.id.toInt() }

        check(pssible == 2913)
    }

    @Test
    fun part2() {
        val lowest = games.map { g ->
            var lowest = Drawn(0, 0, 0)
            g.drawn.forEach { d ->
                if (d.red > lowest.red) {
                    lowest = lowest.copy(red = d.red)
                }
                if (d.green > lowest.green) {
                    lowest = lowest.copy(green = d.green)
                }
                if (d.blue > lowest.blue) {
                    lowest = lowest.copy(blue = d.blue)
                }
            }
            g.id to (lowest.red * lowest.green * lowest.blue)
        }.sumOf { it.second }

        check(lowest == 55593L)
    }
}

data class Game(
    val id: String,
    val drawn: List<Drawn>,
)

data class Drawn(
    val red: Long,
    val green: Long,
    val blue: Long,
)
