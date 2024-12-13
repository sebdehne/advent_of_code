package com.dehnes.adventofcode.utils

import java.io.File
import java.nio.charset.StandardCharsets

object ParserUtils {

    fun getText(): String = getBytes().toString(StandardCharsets.UTF_8)

    fun getLines() = getText().lines()

    private fun getBytes(): ByteArray = File("src/main/resources/${getYear()}/day${getD_a_y()}.txt").readBytes()

    private fun getD_a_y() = Thread.currentThread().stackTrace
        .mapNotNull {
            val str = it.toString()
            if (str.lowercase().contains("day")) str else null
        }
        .flatMap {
            it.lowercase().split("day").map { it.substring(0..1) }.filter { it.all { it.isDigit() } }
        }
        .distinct()
        .single()

    private fun getYear() = Thread.currentThread().stackTrace
        .mapNotNull {
            val str = it.toString()
            if (str.lowercase().contains("day")) str else null
        }.map {
            it.replace("com.dehnes.adventofcode.v", "").split(".")[0]
        }
        .distinct()
        .single()
}