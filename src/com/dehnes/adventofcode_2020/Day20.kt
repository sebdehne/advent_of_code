package com.dehnes.adventofcode_2020

import org.junit.jupiter.api.Test
import java.io.File

class Day20 {

    data class Image(
            val id: Long,
            var data: Array<CharArray>,
            val links: Array<Long?> = Array(4) { null }
    ) {
        fun isPlaced() = links.any { it != null }
    }

    val images: List<Image>
    val imageDim: Int

    init {
        val tmpImages = mutableListOf<Image>()
        File("resources/day20.txt").readText().split("\n\n").forEach { imgStr ->
            val lines = imgStr.split("\n")
            val id = lines[0].substring(5, 9).toLong()
            val img = lines.slice(1 until lines.size).map { imgLine -> imgLine.toCharArray() }.toTypedArray()
            tmpImages.add(Image(id, img))
        }
        images = tmpImages
        imageDim = tmpImages[0].data.size

        alignImages()
    }

    fun alignImages() {
        while (true) {
            var changedSomething = false

            val placedImages = images.filter { it.isPlaced() }.let { if (it.isEmpty()) listOf(images[0]) else it }
            placedImages.forEach { placeImage ->
                images.forEach { otherImage ->
                    if (placeImage.id != otherImage.id && matchAndLink(placeImage, otherImage)) {
                        changedSomething = true
                    }
                }
            }

            if (!changedSomething) {
                break
            }
        }
    }

    fun matchAndLink(placedImage: Image, otherImage: Image): Boolean {
        for (side in 0..3) {
            if (placedImage.links[side] != null) {
                continue
            }

            if (otherImage.isPlaced()) {
                if (matchAndLinkAtSide(placedImage, otherImage, side)) {
                    return true
                }
            } else {
                for (f in 0..1) {
                    for (s in 0..3) {
                        if (matchAndLinkAtSide(placedImage, otherImage, side)) {
                            return true
                        }
                        otherImage.data = copyAndRotate(otherImage.data, 1)
                    }
                    otherImage.data = copyAndFlip(otherImage.data)
                }
            }
        }

        return false
    }

    fun matchAndLinkAtSide(existing: Image, candidate: Image, side: Int) = if (
            copyAndRotate(existing.data, 4 - side)[0]
                    .contentEquals(copyAndRotate(copyAndFlip(candidate.data), side)[0])
    ) {
        candidate.links[(side + 2) % 4] = existing.id
        existing.links[side] = candidate.id
        true
    } else false

    @Test
    fun part1() {
        val corners = images.filter { it.links.count { it != null } == 2 }.map { it.id }
        val p = corners.reduce { acc, image -> acc * image }
        println(p) // 17148689442341
    }

    @Test
    fun part2() {
        val noBorders = images.map {
            it.copy(data = it.data.slice(1 until it.data.size - 1).map { line ->
                line.slice(1 until line.size - 1).toCharArray()
            }.toTypedArray())
        }
        val totalHashes = noBorders.sumBy { img -> img.data.sumBy { line -> line.count { it == '#' } } }
        var mergedImage = mergeToSingleImage(noBorders)
        for (f in 0..1) {
            for (r in 0..3) {

                val patternsCount = patternsCount(
                        mergedImage,
                        listOf(
                                "                  # ".toCharArray(),
                                "#    ##    ##    ###".toCharArray(),
                                " #  #  #  #  #  #   ".toCharArray() // 15
                        ).toTypedArray()
                )
                if (patternsCount > 0) {
                    println("Result=" + (totalHashes - (patternsCount * 15))) // 2009
                    return
                }

                mergedImage = copyAndRotate(mergedImage, 1)
            }
            mergedImage = copyAndFlip(mergedImage)
        }

        error("no match")
    }

    fun mergeToSingleImage(imgs: List<Image>): Array<CharArray> {
        val start = imgs.first {
            it.links[0] == null &&
                    it.links[1] != null &&
                    it.links[2] != null &&
                    it.links[3] == null
        }
        var nextId = start.id
        var nextRowStart: Long? = null
        val result = mutableListOf<CharArray>()
        val imagesRow = mutableListOf<Array<CharArray>>()
        while (true) {
            val i = imgs.first { it.id == nextId }
            if (nextRowStart == null) {
                nextRowStart = i.links[2]
            }

            imagesRow.add(i.data)
            if (i.links[1] != null) {
                nextId = i.links[1]!!
            } else {
                imagesRow[0].indices.forEach { rowIndex ->
                    result.add(
                            imagesRow.fold(charArrayOf()) { acc, img ->
                                acc + img[rowIndex]
                            }
                    )
                }
                imagesRow.clear()
                if (nextRowStart == null) {
                    break
                }
                nextId = nextRowStart
                nextRowStart = null
            }
        }

        return result.toTypedArray()
    }

    fun patternsCount(mergedImage: Array<CharArray>, pattern: Array<CharArray>) =
            mergedImage.toList().windowed(pattern.size).sumBy { windowLines ->
                windowLines[0].indices.windowed(pattern[0].size).count { xWindow ->
                    pattern[0].zip(xWindow.map { windowLines[0][it] }).all { it.first != '#' || it.second == '#' } &&
                            pattern[1].zip(xWindow.map { windowLines[1][it] }).all { it.first != '#' || it.second == '#' } &&
                            pattern[2].zip(xWindow.map { windowLines[2][it] }).all { it.first != '#' || it.second == '#' }
                }
            }

    fun copyAndFlip(img: Array<CharArray>) = img.indices.fold(img.copyData()) { acc, y ->
        acc[(img.size - 1) - y] = img[y]
        acc
    }

    fun copyAndRotate(data: Array<CharArray>, times: Int) = data.copyData().let { rotated ->
        repeat(times) {
            val start = rotated.copyData()

            for (y in rotated.indices) {
                for (x in rotated.indices) {
                    rotated[x][(rotated.size - 1) - y] = start[y][x]
                }
            }
        }
        rotated
    }

    fun Array<CharArray>.copyData() = this.map { l -> l.copyOf() }.toTypedArray()

}