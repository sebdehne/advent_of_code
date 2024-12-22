package com.dehnes.adventofcode.utils

object ListUtils {

    fun <T> iterate(times: Int, init: T, fn: (T) -> T): T {
        var tmp = init
        repeat(times) { tmp = fn(tmp) }
        return tmp
    }

    fun <O> generate(times: Int, fn: () -> O): List<O> = (0..<times).map {
        fn()
    }

}