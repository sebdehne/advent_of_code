package com.dehnes.adventofcode.utils

object ByteUtils {
    fun byteArrayToHexString(b: ByteArray): String {
        var result = ""
        for (i in b.indices) {
            result +=
                ((b[i].toInt() and 0xff) + 0x100).toString(16).substring(1)
        }
        return result
    }
}