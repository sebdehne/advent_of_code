package com.dehnes.adventofcode.utils

import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.CountDownLatch


fun <T, R> List<T>.runParallel(fn: (item: T) ->R): List<R> {
    val cores = Runtime.getRuntime().availableProcessors()
    val results = ConcurrentLinkedQueue<List<R>>()
    val work = ConcurrentLinkedQueue(this.chunked(this.size / cores))
    val cnt = CountDownLatch(cores)
    repeat(cores) {
        Thread {
            while(true) {
                val subList = work.poll() ?: break
                results.offer(subList.map { fn(it) })
            }
            cnt.countDown()
        }.start()
    }
    cnt.await()

    return results.toList().flatten()
}