package io.bitvavo.simulation.models

class Order(
    var id: Int,
    var side: OrderSide,
    var price: Int,
    var quantity: Int,
    var seq: Long
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        val order = other as Order

        if (id != order.id) return false
        if (price != order.price) return false
        if (quantity != order.quantity) return false
        if (seq != order.seq) return false
        if (side != order.side) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + price
        result = 31 * result + quantity
        result = 31 * result + seq.hashCode()
        result = 31 * result + side.hashCode()
        return result
    }
}
