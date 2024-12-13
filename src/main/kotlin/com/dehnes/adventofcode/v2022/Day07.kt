package com.dehnes.adventofcode.v2022

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day07 {

    @Test
    fun run() {
        val root = readFileSystem()

        val part1 = findDirBelow(root, 100000)
        expectThat(part1.sumOf { it.calcSize() }) isEqualTo 1243729

        val missing = 30000000 - (70000000 - root.calcSize())
        val part2 = findDirClosestTo(root, missing)
        expectThat(part2.calcSize()) isEqualTo 4443914
    }

    fun findDirClosestTo(start: File, target: Long): File {
        var cancidate = start

        fun File.findDirClosestToInternal() {
            children.filter { it.isDir }.forEach { c ->
                if (c.calcSize() in target until cancidate.calcSize()) {
                    cancidate = c
                }
            }
            children.forEach { c -> c.findDirClosestToInternal() }
        }

        cancidate.findDirClosestToInternal()

        return cancidate
    }

    fun findDirBelow(current: File, limit: Long): List<File> =
        listOfNotNull(if (current.calcSize() <= limit) current else null) +
                current.children.filter { it.isDir }.flatMap { c -> findDirBelow(c, limit) }

    fun readFileSystem(): File {
        val root = File(true, "/")
        var current = root

        getLines().forEach { line ->
            when {
                line == "$ cd .." -> current = current.parent!!
                line == "$ cd /" -> current = root
                line == "$ ls" -> current.children.clear()
                line.startsWith("$") -> current = current.children.first { it.name == line.split(" ")[2] }
                else -> {
                    val (typeOrSize, name) = line.split(" ")
                    current.children += if (typeOrSize == "dir") {
                        File(true, name, current)
                    } else {
                        File(false, name, current, typeOrSize.toLong())
                    }
                }
            }
        }

        return root
    }

}

data class File(
    var isDir: Boolean,
    var name: String,
    var parent: File? = null,
    var size: Long = 0,
    val children: MutableList<File> = mutableListOf()
) {
    fun calcSize(): Long = size + children.sumOf { it.calcSize() }
}
