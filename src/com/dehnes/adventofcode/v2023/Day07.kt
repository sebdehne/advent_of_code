package com.dehnes.adventofcode.v2023

import org.junit.jupiter.api.Test
import java.io.File

class Day07 {

    val lines = File("resources/2023/day07.txt").readLines()

    @Test
    fun run() {
        // part1
        check(calc("AKQJT98765432", ::toTypeV1) == 248453531L)

        // part2
        check(calc("AKQT98765432J", ::calcTypeV2) == 248781813L)
    }

    private fun calc(cardToRankPart: String, handToType: (h: List<Char>) -> Type) = lines
        .map { line ->
            val (handStr, bid) = line.split(" ")
            handStr.toList().let { Hand(it, handToType(it)) } to bid.toLong()
        }
        .sortedWith { o1, o2 -> o1.first.compare(cardToRankPart, o2.first) }
        .mapIndexed { index, pair -> pair.second * (index + 1) }
        .sum()

    private fun toTypeV1(hand: List<Char>) = hand.groupBy { it }.entries.map { it.key to it.value.size }
        .let { occurrences ->
            when {
                occurrences.size == 1 -> Type.FiveOfAKind
                occurrences.size == 2 && occurrences.any { it.second == 4 } -> Type.FourOfAKind
                occurrences.size == 2 -> Type.FullHouse
                occurrences.size == 3 && occurrences.any { it.second == 3 } -> Type.ThreeOfAKind
                occurrences.size == 3 -> Type.TwoPair
                occurrences.size == 4 -> Type.OnePair
                else -> Type.HighCard
            }
        }

    private fun calcTypeV2(hand: List<Char>): Type {
        val jokers = hand.count { it == 'J' }

        if (jokers == 0) return toTypeV1(hand)

        val occurrences = hand
            .filterNot { it == 'J' }
            .groupBy { it }
            .entries
            .map { it.key to it.value.size }
            .sortedByDescending { it.second }

        return when {
            occurrences.size <= 1 -> Type.FiveOfAKind
            (5 - occurrences.first().second - jokers) == 1 -> Type.FourOfAKind
            occurrences.size == 2 -> Type.FullHouse
            occurrences.first().second + jokers == 3 -> Type.ThreeOfAKind
            else -> Type.OnePair
        }
    }

    data class Hand(
        val cards: List<Char>,
        val type: Type,
    ) {

        fun compare(cardToRank: String, other: Hand): Int {
            val compareTo = type.rank.compareTo(other.type.rank)
            if (compareTo != 0) return compareTo

            cards.zip(other.cards)
                .map { cardToRank.indexOf(it.first) to cardToRank.indexOf(it.second) }
                .forEach { pair ->
                    val compareTo1 = pair.second.compareTo(pair.first)
                    if (compareTo1 != 0) return compareTo1
                }

            return 0
        }
    }

    enum class Type(val rank: Int) {
        FiveOfAKind(7), FourOfAKind(6), FullHouse(5), ThreeOfAKind(4), TwoPair(3), OnePair(2), HighCard(1),
    }

}