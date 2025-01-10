package io.bitvavo.simulation.exchange.mappers

import io.bitvavo.simulation.models.Order
import io.bitvavo.simulation.models.OrderSide

object OrderMapper {
    fun String.tryParseOrder(seq: Long): Result<Order> {
        val parts = this.split(",")
        if (parts.size != 4)
            return Result.failure(IllegalArgumentException("Invalid order format: '$this'"))

        val id = parts[0].toIntOrNull()
            ?: return Result.failure(IllegalArgumentException("Invalid order ID: '${parts[0]}'"))

        if (parts[1] != "B" && parts[1] != "S")
            return Result.failure(IllegalArgumentException("Invalid order side: '${parts[1]}'"))

        val side = if (parts[1] == "B") OrderSide.Buy else OrderSide.Sell

        val price = parts[2].toIntOrNull()
            ?: return Result.failure(IllegalArgumentException("Invalid order price: '${parts[2]}'"))

        val quantity = parts[3].toIntOrNull()
            ?: return Result.failure(IllegalArgumentException("Invalid order quantity: '${parts[3]}'"))

        val order = Order(
            id = id,
            side = side,
            price = price,
            quantity = quantity,
            seq = seq
        )

        return Result.success(order)
    }
}