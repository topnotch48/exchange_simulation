package io.bitvavo.simulation.exchange.sequence_generator

interface SequenceNumberGenerator {
    fun next(): Long
}