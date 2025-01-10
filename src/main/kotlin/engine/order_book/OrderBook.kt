package io.bitvavo.simulation.engine.order_book

import io.bitvavo.simulation.models.Order
import io.bitvavo.simulation.models.OrderBookSnapshot

interface OrderBook {
    fun contains(orderId: Int): Boolean
    fun add(order: Order): Boolean
    fun getTopBid(): Order?
    fun getTopAsk(): Order?
    fun getOrderBookSnapshot(): OrderBookSnapshot
}