package com.dehnes.adventofcode.utils

import com.dehnes.adventofcode.utils.ByteUtils.byteArrayToHexString
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object Hash {

    fun SHA1(data: ByteArray): String {
        var md: MessageDigest? = null
        try {
            md = MessageDigest.getInstance("SHA-1")
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return byteArrayToHexString(md!!.digest(data))
    }

}