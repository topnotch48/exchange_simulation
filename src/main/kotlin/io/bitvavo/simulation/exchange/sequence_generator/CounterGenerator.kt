package io.bitvavo.simulation.exchange.sequence_generator

class CounterGenerator : SequenceNumberGenerator {
    private var counter: Long = 0
    override fun next(): Long = ++counter
}