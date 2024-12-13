package com.dehnes.adventofcode.v2023

import com.dehnes.adventofcode.utils.MathUtils.lcm
import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test
import java.util.*

class Day20 {

    interface Module {
        fun signal(source: String, s: Boolean): Boolean?

        val name: String

        val outConnections: Set<String>
        val inConnections: Set<String>

        fun reset()
    }

    data class FlipFlop(
        override val name: String,
        override val outConnections: Set<String>,
        override val inConnections: Set<String>,
        var state: Boolean = false
    ) : Module {


        override fun reset() {
            state = false
        }

        override fun signal(source: String, s: Boolean) = if (!s) {
            state = !state
            state
        } else {
            null
        }
    }

    data class Conjunction(
        override val name: String,
        override val outConnections: Set<String>,
        override val inConnections: Set<String>,
    ) : Module {
        var states = inConnections.associateWith { false }.toMutableMap()
        override fun reset() {
            states = inConnections.associateWith { false }.toMutableMap()
        }

        override fun signal(source: String, s: Boolean): Boolean {
            states[source] = s

            return !states.values.all { it }
        }
    }

    data class Broadcaster(
        override val outConnections: Set<String>,
        override val inConnections: Set<String>,
    ) : Module {
        override fun signal(source: String, s: Boolean): Boolean {
            return s
        }

        override fun reset() {
        }

        override val name: String
            get() = "broadcaster"
    }

    val lines = getLines().map {
        val (l, r) = it.split("->").map { it.trim() }
        val outConnectionNames = r.split(",").map { it.trim() }
        l to outConnectionNames
    }.toMap()

    private val modules = lines.map { (typeWithName, outConnections) ->

        if (typeWithName == "broadcaster") {
            val inConnections = lines.filter { (_, outConnections) ->
                outConnections.any { it == typeWithName }
            }.map { it.key.drop(1) }

            Broadcaster(outConnections.toSet(), inConnections.toSet())
        } else {
            val type = typeWithName[0]
            val name = typeWithName.drop(1)

            val inConnections = lines.filter { (_, outConnections) ->
                outConnections.any { it == name }
            }.map {
                if (it.key != "broadcaster") it.key.drop(1) else it.key
            }

            when (type) {
                '%' -> FlipFlop(name, outConnections.toSet(), inConnections.toSet())
                '&' -> Conjunction(name, outConnections.toSet(), inConnections.toSet())
                else -> error("")
            }
        }
    }.associateBy { it.name }

    private fun handleSignal(
        initialSignal: Boolean,
        ifModuleHigh: Set<String> = emptySet(),
        ifModuleHit: (n: String) -> Unit = {}
    ): Pair<Long, Long> {
        var counterLow = 0L
        var counterHigh = 0L
        val todo = LinkedList<Pair<String, Boolean>>()
        todo.offer("broadcaster" to initialSignal)

        fun count(s: Boolean) {
            if (s) counterHigh++ else counterLow++
        }
        count(initialSignal)

        while (todo.isNotEmpty()) {
            val (m, s) = todo.poll()
            if (m in ifModuleHigh && s) {
                ifModuleHit(m)
            }
            val module = modules[m]!!
            module.outConnections.forEach { dst ->
                count(s)
                modules[dst]?.signal(m, s)?.let {
                    todo.offer(dst to it)
                }
            }
        }

        return counterLow to counterHigh
    }

    @Test
    fun run() {
        var cLow = 0L
        var cHigh = 0L
        repeat(1000) {
            handleSignal(false).let {
                cLow += it.first
                cHigh += it.second
            }
        }

        check(cLow * cHigh == 806332748L)

        modules.forEach { (_, u) ->
            u.reset()
        }

        val sources = modules["xn"]!!.inConnections

        var cnt = 0
        val sourcesHighAt = mutableMapOf<String, Int>()
        while (sourcesHighAt.size < sources.size) {
            cnt++
            handleSignal(false, sources) {
                sourcesHighAt[it] = cnt
            }
        }

        check(sourcesHighAt.values.fold(1L) { acc, a -> lcm(acc, a.toLong()) } == 228060006554227L)
    }
}