package com.dehnes.adventofcode.v2024

import com.dehnes.adventofcode.utils.ParserUtils.getText
import org.junit.jupiter.api.Test

class Day03 {


    @Test
    fun run() {
        check(eval2(false) == 178794710L)
        check(eval2(true) == 76729637L)
    }

    private fun eval2(isPart2: Boolean) =
        "(mul\\(\\d{1,3},\\d{1,3}\\))|(do\\(\\))|(don't\\(\\))".toRegex().findAll(getText())
            .fold(true to 0L) { (enabled, result), m ->
                when (val gr = m.groupValues.first()) {
                    "do()" -> true to result
                    "don't()" -> !isPart2 to result
                    else -> if (enabled) {
                        val (l, r) = gr.replace("mul(", "").replace(")", "").split(",").map { it.toInt() }
                        true to (result + l * r)
                    } else {
                        false to result
                    }
                }
            }.second
}