package com.dehnes.adventofcode.v2019

import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test
import java.util.LinkedList

class Day14 {

    val reactions = getLines().map {
        val (inputStr, outputStr) = it.split(" => ")
        val inputs = inputStr.split(",").map { it.trim() }.map {
            val (q, c) = it.split(" ")
            ChemicalAndQuantity(c, q.toLong())
        }
        val (outputQ, outputC) = outputStr.split(" ")
        Reaction(
            inputs,
            ChemicalAndQuantity(
                outputC,
                outputQ.toLong()
            )
        )
    }

    val reactionsByOutcome = reactions.associateBy { it.output.chemical }


    @Test
    fun test() {
        val oneFuelCost = 899155L
        check(calc(1) == oneFuelCost)
        check(calc(2390226) == 999999853464)
    }

    fun calc(fuelAmount: Long): Long {
        val inventory = mutableMapOf<String, Long>()
        var oreConsumed = 0L

        val stack = LinkedList<Request>()
        val fuelReaction = reactionsByOutcome["FUEL"]!!
        stack.addFirst(fuelReaction.request(fuelAmount))

        val logging = mutableMapOf<Reaction, Long>()

        while (stack.isNotEmpty()) {
            val next = stack.peek()

            val unresolveInput = next.unresolvedInputs.entries.firstOrNull { it.value > 0 }
            if (unresolveInput == null) {
                stack.pop()
                inventory[next.target.output.chemical] =
                    inventory.getOrDefault(next.target.output.chemical, 0) + (next.rounds * next.target.output.quantity)
                logging[next.target] = logging.getOrDefault(next.target, 0) + next.rounds
                continue
            }

            val inventoryAmount = inventory[unresolveInput.key]
            if (inventoryAmount != null) {
                if (inventoryAmount == unresolveInput.value) {
                    next.unresolvedInputs.remove(unresolveInput.key)
                    inventory.remove(unresolveInput.key)
                } else if (inventoryAmount > unresolveInput.value) {
                    next.unresolvedInputs.remove(unresolveInput.key)
                    inventory[unresolveInput.key] = inventoryAmount - unresolveInput.value
                } else {
                    inventory.remove(unresolveInput.key)
                    next.unresolvedInputs[unresolveInput.key] = unresolveInput.value - inventoryAmount
                }
                continue
            }

            if (unresolveInput.key == "ORE") {
                val r = next.target
                check(r.inputs.size == 1)
                val rounds = divideRoundUp(unresolveInput.value, r.output.quantity)
                val oreProduced = rounds * r.inputs.single().quantity
                oreConsumed += oreProduced
                inventory["ORE"] = oreProduced + inventory.getOrDefault("ORE", 0)
                continue
            }


            val r = reactionsByOutcome[unresolveInput.key]!!
            stack.addFirst(r.request(unresolveInput.value))
        }

        return logging.filter { it.key.inputs.size == 1 && it.key.inputs.single().chemical == "ORE" }
            .map { it.key.inputs.single().quantity * it.value }.sum()

    }


    companion object {

        fun divideRoundUp(a: Long, b: Long): Long {
            val hasRest = a % b > 0
            return (a / b) + (if (hasRest) 1 else 0)
        }

        class Request(
            val target: Reaction,
            val unresolvedInputs: MutableMap<String, Long>,
            val rounds: Long,
        )

        data class Reaction(
            val inputs: List<ChemicalAndQuantity>,
            val output: ChemicalAndQuantity
        )

        fun Reaction.request(quantityNeeded: Long): Request {
            val rounds = divideRoundUp(quantityNeeded, output.quantity)
            return Request(
                this,
                inputs.associate { it.chemical to (it.quantity * rounds) }.toMutableMap(),
                rounds
            )
        }

        data class ChemicalAndQuantity(
            val chemical: String,
            val quantity: Long,
        )
    }
}