package com.dehnes.adventofcode.v2019

import java.io.File

fun inputText(day: Int) = File(fileName(day)).readText()
fun inputLines(day: Int) = File(fileName(day)).readLines()

private fun fileName(day: Int) = "resources/2019/day" + day.toString().padStart(2, '0') + ".txt"

