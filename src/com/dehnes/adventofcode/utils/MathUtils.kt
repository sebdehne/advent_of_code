package com.dehnes.adventofcode.utils

object MathUtils {
    fun gcd(n1: Long, n2: Long): Long = if (n2 != 0L) gcd(n2, n1 % n2) else n1
    fun lcm(a: Long, b: Long) = (a*b)/gcd(a,b)
}