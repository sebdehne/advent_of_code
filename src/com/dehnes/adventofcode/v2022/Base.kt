package com.dehnes.adventofcode.v2022

import java.io.File

fun inputText(day: Int) = File(fileName(day)).readText()
fun inputLines(day: Int) = File(fileName(day)).readLines()

private fun fileName(day: Int) = "resources/2022/day" + day.toString().padStart(2, '0') + ".txt"

