package com.dehnes.adventofcode.utils

object RangeUtils {

    fun IntRange?.len() = if (this == null) 0L else (this.last - this.first + 1).toLong()

    fun LongRange.offset(d: Long) = (this.first + d)..(this.last + d)
    fun LongRange.overlaps(other: LongRange): Boolean {
        if (other.first > last) return false
        if (other.last < first) return false
        return true
    }

    data class CombineResult(
        val r: LongRange,
        val isCommon: Boolean,
    )

    fun LongRange.combine(other: LongRange): List<CombineResult> {
        val r = mutableListOf<CombineResult>()

        when {
            // other starts before and ends inside
            other.first !in this && other.last in this -> {
                r.add(
                    CombineResult(
                        other.first..<first,
                        false
                    )
                )
                r.add(
                    CombineResult(
                        first..other.last,
                        true
                    )
                )
                if (other.last < last) {
                    r.add(
                        CombineResult(
                            (other.last + 1)..last,
                            false
                        )
                    )
                }
            }
            // other starts inside and ends after
            other.first in this && other.last !in this -> {
                if (first < other.first) {
                    r.add(
                        CombineResult(
                            first..<other.first,
                            false
                        )
                    )
                }
                r.add(
                    CombineResult(
                        other.first..last,
                        true
                    )
                )
                r.add(
                    CombineResult(
                        (last + 1)..other.last,
                        false
                    )
                )

            }
            // other inside this
            other.first in this && other.last in this -> {
                if (first < other.first) {
                    r.add(
                        CombineResult(
                            first..<other.first,
                            false
                        )
                    )
                }
                r.add(
                    CombineResult(
                        other,
                        true
                    )
                )
                if (last > other.last) {
                    r.add(
                        CombineResult(
                            (other.last + 1)..last,
                            false
                        )
                    )
                }

            }
            // other outside this
            other.first !in this && other.last !in this -> {
                if (other.first < first) {
                    r.add(
                        CombineResult(
                            other.first..<first,
                            false
                        )
                    )
                }

                r.add(
                    CombineResult(
                        this,
                        true
                    )
                )
                if (last < other.last) {
                    r.add(
                        CombineResult(
                            (last + 1)..other.last,
                            false
                        )
                    )
                }

            }
        }

        return r
    }

}