package io.bitvavo.simulation.engine

import io.bitvavo.simulation.models.Order
import io.bitvavo.simulation.models.OrderBookSnapshot
import io.bitvavo.simulation.models.OrderMatchResult

interface MatchingEngine {
    fun tryMatch(order: Order): OrderMatchResult
    fun getOrderBookSnapshot(): OrderBookSnapshot
}