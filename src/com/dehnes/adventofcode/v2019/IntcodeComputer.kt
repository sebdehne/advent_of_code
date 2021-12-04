package com.dehnes.adventofcode.v2019

fun intcodeComputer(inputCode: List<Long>, inputSource: () -> Long, output: (Long) -> Unit) {
    val state = State.init(inputCode)

    var i: Long? = null
    while (true) {
        val r = intcodeComputer_(state, i)
        i = null
        when (r) {
            is Terminated -> return
            is InputNeeded -> i = inputSource()
            is Output -> output(r.value)
        }
    }
}

class State(
        val inputCode: MutableMap<Long, Long>,
        var index: Long,
        var relativeBase: Long
) {
    companion object {
        fun init(code: List<Long>) = State(
                code.mapIndexed { index, i ->
                    index.toLong() to i
                }.toMap().toMutableMap(),
                0,
                0
        )
    }
}

sealed class Result
data class Output(val value: Long) : Result()
object InputNeeded : Result()
object Terminated : Result()

fun intcodeComputer_(state: State, input: Long?): Result {
    val code = state.inputCode
    var usingInput: Long? = input
    while (true) {
        val opcode = code[state.index].toString().padStart(5, '0')
        val op = opcode.substring(3).toInt()
        val modes = opcode.substring(0, 3).reversed().toCharArray()
        val addr = { n: Int ->
            when (modes[n - 1]) {
                '0' -> code.getOrElse(state.index + n) { 0 }
                '1' -> state.index + n
                '2' -> code.getOrElse(state.index + n) { 0 } + state.relativeBase
                else -> error("")
            }
        }

        when (op) {
            1 -> {
                code[addr(3)] = code.getOrElse(addr(1)) { 0 } + code.getOrElse(addr(2)) { 0 }
                state.index += 4
            }
            2 -> {
                code[addr(3)] = code.getOrElse(addr(1)) { 0 } * code.getOrElse(addr(2)) { 0 }
                state.index += 4
            }
            3 -> {
                if (usingInput != null) {
                    code[addr(1)] = usingInput
                    usingInput = null
                    state.index += 2
                } else {
                    return InputNeeded
                }
            }
            4 -> {
                val o = code.getOrElse(addr(1)) { 0 }
                state.index += 2
                return Output(o)
            }
            5 -> {
                if (code.getOrElse(addr(1)) { 0 } != 0L) {
                    state.index = code.getOrElse(addr(2)) { 0 }
                } else {
                    state.index += 3
                }
            }
            6 -> {
                if (code.getOrElse(addr(1)) { 0 } == 0L) {
                    state.index = code.getOrElse(addr(2)) { 0 }
                } else {
                    state.index += 3
                }
            }
            7 -> {
                if (code.getOrElse(addr(1)) { 0 } < code.getOrElse(addr(2)) { 0 }) {
                    code[addr(3)] = 1
                } else {
                    code[addr(3)] = 0
                }
                state.index += 4
            }
            8 -> {
                if (code.getOrElse(addr(1)) { 0 } == code.getOrElse(addr(2)) { 0 }) {
                    code[addr(3)] = 1
                } else {
                    code[addr(3)] = 0
                }
                state.index += 4
            }
            9 -> {
                state.relativeBase += code.getOrElse(addr(1)) { 0 }
                state.index += 2
            }
            99 -> return Terminated
            else -> error("?: $op")
        }
    }
}