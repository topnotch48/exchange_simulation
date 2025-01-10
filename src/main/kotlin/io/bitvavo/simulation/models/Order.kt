package io.bitvavo.simulation.models

class Order(
    var id: Int,
    var side: OrderSide,
    var price: Int,
    var quantity: Int,
    var timestampMs: Long
)
