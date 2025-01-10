package io.bitvavo.simulation.exchange.clock

interface Clock {
    fun currentTimeMillis(): Long
}