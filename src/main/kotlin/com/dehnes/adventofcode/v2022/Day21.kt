package com.dehnes.adventofcode.v2022

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test
import java.util.*

class Day21 {


    @Test
    fun part1() {
        val dependencies = mutableMapOf<String, Any>()
        getLines().forEach { line ->
            val (monkey, task) = line.split(":")
            val parts = task.trim().split(" ")
            if (parts.size == 1) {
                dependencies[monkey] = parts.first().toLong()
            } else {
                dependencies[monkey] = Dependency(
                    parts[0],
                    parts[2],
                    parts[1][0],
                )
            }
        }

        val dependenciesStack = LinkedList<String>()
        dependenciesStack.add("root")
        while (dependenciesStack.isNotEmpty()) {
            val next = dependenciesStack.removeLast()

            val d = dependencies[next] as Dependency
            val newStack = LinkedList<String>()

            if (d.left is String) {
                val s = d.left as String
                if (dependencies[s] is Long) {
                    d.left = dependencies[s] as Long
                } else {
                    newStack.addLast(d.left as String)
                }
            }
            if (d.right is String) {
                val s = d.right as String
                if (dependencies[s] is Long) {
                    d.right = dependencies[s] as Long
                } else {
                    newStack.addLast(d.right as String)
                }
            }

            if (d.left is Long && d.right is Long) {
                // resolve
                dependencies[next] = when (d.op) {
                    '+' -> (d.left as Long) + d.right as Long
                    '-' -> (d.left as Long) - d.right as Long
                    '*' -> (d.left as Long) * d.right as Long
                    '/' -> (d.left as Long) / d.right as Long
                    else -> error("")
                }
            } else {
                newStack.addFirst(next)
            }

            dependenciesStack.addAll(newStack)
        }

        check(dependencies["root"] as Long == 110181395003396L)
    }

    @Test
    fun part2() {
        val target = 22931068684876L

        var start = 1000000000000L
        var end = 10000000000000L
        var result = 0L

        while (result == 0L) {
            val delta = end - start
            val half = start + (delta / 2)
            val r = solve("tlpd", "humn" to half)
            if (r == target) {
                result = half
            }
            if (r > target) {
                start = half
            } else {
                end = half
            }
        }
        check(result == 3721298272959L)
    }

    fun solve(start: String, test: Pair<String, Long>): Long {
        val dependencies = mutableMapOf<String, Any>()
        getLines().forEach { line ->
            val (monkey, task) = line.split(":")
            val parts = task.trim().split(" ")
            if (parts.size == 1) {
                dependencies[monkey] = parts.first().toLong()
            } else {
                dependencies[monkey] = Dependency(
                    parts[0],
                    parts[2],
                    parts[1][0],
                )
            }
        }

        dependencies[test.first] = test.second
        val dependenciesStack = LinkedList<String>()
        dependenciesStack.add(start)
        while (dependenciesStack.isNotEmpty()) {
            val next = dependenciesStack.removeLast()

            val d = dependencies[next] as Dependency
            val newStack = LinkedList<String>()

            if (d.left is String) {
                val s = d.left as String
                if (dependencies[s] is Long) {
                    d.left = dependencies[s] as Long
                } else {
                    newStack.addLast(d.left as String)
                }
            }
            if (d.right is String) {
                val s = d.right as String
                if (dependencies[s] is Long) {
                    d.right = dependencies[s] as Long
                } else {
                    newStack.addLast(d.right as String)
                }
            }

            if (d.left is Long && d.right is Long) {
                // resolve
                dependencies[next] = when (d.op) {
                    '+' -> (d.left as Long) + d.right as Long
                    '-' -> (d.left as Long) - d.right as Long
                    '*' -> (d.left as Long) * d.right as Long
                    '/' -> (d.left as Long) / d.right as Long
                    else -> error("")
                }
            } else {
                newStack.addFirst(next)
            }

            dependenciesStack.addAll(newStack)
        }

        return dependencies[start] as Long
    }
}

data class Dependency(
    var left: Any,
    var right: Any,
    val op: Char,
)
