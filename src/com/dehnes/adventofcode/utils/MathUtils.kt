package com.dehnes.adventofcode.utils

object MathUtils {
    fun gcd(n1: Long, n2: Long): Long = if (n2 != 0L) gcd(n2, n1 % n2) else n1
    fun lcm(a: Long, b: Long) = (a*b)/gcd(a,b)

    // https://www.quora.com/How-do-I-write-a-Java-program-that-will-solve-simultaneous-equations
    fun solveForTwoUnknowns(a1: Double, b1: Double, c1: Double, a2: Double, b2: Double, c2: Double): Pair<Double, Double>? {
        // Calculate the determinant of the coefficient matrix
        val D = a1 * b2 - a2 * b1

        // Check if the determinant is zero
        if (D == 0.0) {
            return null
        }

        // Calculate determinants for Cramer's Rule
        val Dx = c1 * b2 - c2 * b1 // Determinant for x
        val Dy = a1 * c2 - a2 * c1 // Determinant for y

        // Calculate the values of x and y
        val x = Dx / D
        val y = Dy / D

        // Output the solution
        return x to y
    }
}