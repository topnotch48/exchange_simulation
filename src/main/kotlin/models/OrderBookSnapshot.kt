package io.bitvavo.simulation.models

data class OrderBookSnapshot(
    val bids: List<Order>,
    val asks: List<Order>
)