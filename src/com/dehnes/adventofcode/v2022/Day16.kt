package com.dehnes.adventofcode.v2022

import com.dehnes.adventofcode.utils.Dijkstra
import com.dehnes.adventofcode.utils.ParserUtils.getLines
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.isEqualTo

class Day16 {
    val map: MutableMap<String, Valve>
    val distances: Map<Pair<String, String>, Int>

    init {
        val mapTmp = mutableMapOf<String, Valve>()
        getLines().forEach { line ->
            val parts = line.split(" ", ";", "=", ",").filter { it.isNotBlank() }
            mapTmp[parts[1]] = Valve(
                parts[1],
                parts[5].toInt(),
                parts.drop(10).associateWith { 1 }
            )
        }
        map = mapTmp


        val distancesTmp = mutableMapOf<Pair<String, String>, Int>()
        map.keys.forEach { from ->
            map.keys.forEach { to ->
                val p1 = from to to
                val p2 = to to from
                if (p1 !in distancesTmp && p2 !in distancesTmp) {
                    if (from == to) {
                        distancesTmp[p1] = 0
                        distancesTmp[p2] = 0
                    } else {
                        val d2 = Dijkstra.solve(from) { n->
                            map.values.first { it.id == n }.connections.entries.map {
                                it.key to it.value
                            }
                        }

                        val dist = d2[to]!!.toInt()
                        distancesTmp[p1] = dist
                        distancesTmp[p2] = dist
                    }
                }

            }
        }

        distances = distancesTmp
    }


    @Test
    fun part1() {

        val keep = 100
        val timelimit = 30

        var paths = listOf(
            PathTaken(
                emptyList(),
                emptyMap(),
                0,
                0,
                "AA"
            )
        )

        val doneList = mutableListOf<PathTaken>()

        while (paths.isNotEmpty()) {

            if (paths.size > keep) {
                val sorted =
                    paths.sortedByDescending { ((timelimit - it.minutesSpent) * it.currentlyOpen.values.sum()) + it.released }
                paths = sorted.subList(0, keep)
            }

            val (more, done) = paths.partition { it.minutesSpent < timelimit }
            doneList.addAll(done)

            val nextPaths = mutableListOf<PathTaken>()
            more.forEach { p ->

                val unopenedValves = map.filter { it.value.rate > 0 }.filter {
                    it.key !in p.currentlyOpen
                }.keys

                var added = false
                unopenedValves.forEach { uv ->
                    val distance: Int = distances[p.pos to uv]!!
                    if (p.minutesSpent + distance + 1 <= timelimit) {
                        added = true
                        nextPaths.add(
                            p.copy(
                                actions = p.actions + Walked(p.pos, uv, distance) + OpenedValve(uv),
                                currentlyOpen = p.currentlyOpen + (uv to map[uv]!!.rate),
                                released = p.released + ((distance + 1) * p.currentlyOpen.values.sum()),
                                minutesSpent = p.minutesSpent + (distance + 1),
                                pos = uv
                            )
                        )
                    }

                }

                if (!added) { // not moved, await the rest
                    val remaining = timelimit - p.minutesSpent
                    nextPaths.add(
                        p.copy(
                            actions = p.actions + Wait,
                            released = p.released + (remaining * p.currentlyOpen.values.sum()),
                            minutesSpent = p.minutesSpent + remaining,
                        )
                    )
                }

            }

            paths = nextPaths
        }


        val max = doneList.maxByOrNull { it.released }!!
        expectThat(max.released) isEqualTo 2183
    }

    @Test
    fun part2() {
        val keep = 1000000
        val timelimit = 26

        var paths = listOf(
            PathTaken2(
                emptyList(),
                emptyList(),
                emptyMap(),
                0,
                0,
                emptySet()
            )
        )

        val doneList = mutableListOf<PathTaken2>()


        while (paths.isNotEmpty()) {
            if (paths.size > keep) {
                val sorted = paths.sortedByDescending { p ->
                    val r = timelimit - p.minutesSpent
                    p.released + (r * p.currentlyOpen.values.sum())
                }
                paths = sorted.subList(0, keep)
            }

            val (more, done) = paths.partition { it.minutesSpent < timelimit }
            doneList.addAll(done)

            val nextPaths = mutableListOf<PathTaken2>()
            more.forEach { p ->
                val remainingTime = timelimit - p.minutesSpent

                if (p.actionsMe.isEmpty() || p.actionsMe.last().remaining == 0) {
                    val last = p.actionsMe.lastOrNull()
                    if (last is TimedWalk) {
                        // open
                        nextPaths.add(
                            p.copy(
                                actionsMe = p.actionsMe + TimedOpen(last.to, 1)
                            )
                        )
                    } else {
                        // walk
                        val walk = p.actionsMe.lastOrNull { it is TimedWalk } as? TimedWalk
                        val pos = walk?.to ?: "AA"

                        val candidates = map
                            .filter { it.value.rate > 0 }
                            .filter { it.key !in p.currentlyOpen }
                            .filter { it.key !in p.valuesTaken }
                            .keys
                            .map { it to distances[pos to it]!! }
                            .filter { (it.second + 1) <= remainingTime }

                        if (candidates.isEmpty()) {
                            nextPaths.add(p.copy(actionsMe = p.actionsMe + TimedWait(remainingTime)))
                        } else {
                            candidates.forEach { (dst, dist) ->
                                nextPaths.add(
                                    p.copy(
                                        actionsMe = p.actionsMe + TimedWalk(pos, dst, dist, dist),
                                        valuesTaken = p.valuesTaken + dst
                                    )
                                )
                            }
                        }
                    }
                } else if (p.actionsEl.isEmpty() || p.actionsEl.last().remaining == 0) {


                    val last = p.actionsEl.lastOrNull()
                    if (last is TimedWalk) {
                        // open
                        nextPaths.add(
                            p.copy(
                                actionsEl = p.actionsEl + TimedOpen(last.to, 1)
                            )
                        )
                    } else {
                        // walk
                        val walk = p.actionsEl.lastOrNull { it is TimedWalk } as? TimedWalk
                        val pos = walk?.to ?: "AA"

                        val candidates = map
                            .filter { it.value.rate > 0 }
                            .filter { it.key !in p.currentlyOpen }
                            .filter { it.key !in p.valuesTaken }
                            .keys
                            .map { it to distances[pos to it]!! }
                            .filter { (it.second + 1) <= remainingTime }

                        if (candidates.isEmpty()) {
                            nextPaths.add(p.copy(actionsEl = p.actionsEl + TimedWait(remainingTime)))
                        } else {
                            candidates.forEach { (dst, dist) ->
                                nextPaths.add(
                                    p.copy(
                                        actionsEl = p.actionsEl + TimedWalk(pos, dst, dist, dist),
                                        valuesTaken = p.valuesTaken + dst
                                    )
                                )
                            }
                        }
                    }

                } else {
                    // consume time
                    val lastMe = p.actionsMe.last()
                    val lastEl = p.actionsEl.last()

                    val timeToSpend = Integer.min(
                        lastMe.remaining,
                        lastEl.remaining
                    )

                    val opened = mutableListOf<Pair<String, Int>>()
                    if (lastMe is TimedOpen && (lastMe.remaining - timeToSpend) == 0) {
                        opened.add(lastMe.id to map[lastMe.id]!!.rate)
                    }
                    if (lastEl is TimedOpen && (lastEl.remaining - timeToSpend) == 0) {
                        opened.add(lastEl.id to map[lastEl.id]!!.rate)
                    }

                    nextPaths.add(
                        p.copy(
                            actionsMe = p.actionsMe.dropLast(1) + lastMe.spend(timeToSpend),
                            actionsEl = p.actionsEl.dropLast(1) + lastEl.spend(timeToSpend),
                            released = p.released + (timeToSpend * p.currentlyOpen.values.sum()),
                            minutesSpent = p.minutesSpent + timeToSpend,
                            currentlyOpen = p.currentlyOpen + opened
                        )
                    )
                }

            }
            paths = nextPaths

        }


        val max = doneList.maxByOrNull { it.released }!!
        val maxReleased = max.released
        expectThat(maxReleased) isEqualTo 2911

    }
}

data class PathTaken(
    val actions: List<Action16>,
    val currentlyOpen: Map<String, Int>,
    val released: Int,
    val minutesSpent: Int,
    val pos: String,
)

sealed class Action16
data class OpenedValve(
    val id: String
) : Action16()

data class Walked(
    val from: String,
    val to: String,
    val minutesSpent: Int,
) : Action16()

object Wait : Action16()

data class Valve(
    val id: String,
    val rate: Int,
    val connections: Map<String, Long>,
)

data class PathTaken2(
    val actionsMe: List<TimedAction>,
    val actionsEl: List<TimedAction>,
    val currentlyOpen: Map<String, Int>,
    val released: Int,
    val minutesSpent: Int,
    val valuesTaken: Set<String>
)

sealed class TimedAction {
    abstract val remaining: Int

    abstract fun spend(time: Int): TimedAction
}

data class TimedWalk(
    val from: String,
    val to: String,
    val distance: Int,
    override var remaining: Int
) : TimedAction() {
    override fun spend(time: Int) = copy(remaining = remaining - time)
}

data class TimedOpen(
    val id: String,
    override var remaining: Int
) : TimedAction() {
    override fun spend(time: Int) = copy(remaining = remaining - time)
}

data class TimedWait(
    override var remaining: Int
) : TimedAction() {
    override fun spend(time: Int) = copy(remaining = remaining - time)
}