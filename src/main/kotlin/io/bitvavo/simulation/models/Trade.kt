package io.bitvavo.simulation.models

data class Trade(
    val aggressionOrderId: Int,
    val restingOrderId: Int,
    val matchedPrice: Int,
    val quantity: Int
)