package io.bitvavo.simulation.formatters

import io.bitvavo.simulation.models.OrderBookSnapshot

object OrderBookFormatter {
    fun formatOrderBook(orderBook: OrderBookSnapshot): String {
        val rows = mutableListOf<String>()
        val bids = orderBook.bids
        val asks = orderBook.asks
        val maxRows = maxOf(bids.size, asks.size)

        val quantityPlaceholder = " ".repeat(11)
        val pricePlaceholder = " ".repeat(6)

        for (i in 0 until maxRows) {
            val bid = bids.getOrNull(i)
            val ask = asks.getOrNull(i)

            val bidQuantity = bid?.quantity?.formatQuantity() ?: quantityPlaceholder
            val bidPrice = bid?.price?.formatPrice() ?: pricePlaceholder

            val askPrice = ask?.price?.formatPrice() ?: pricePlaceholder
            val askQuantity = ask?.quantity?.formatQuantity() ?: quantityPlaceholder

            rows.add("$bidQuantity $bidPrice | $askPrice $askQuantity")
        }

        return rows.joinToString("\n")
    }

    private fun Int.formatQuantity(): String {
        return String.format("%,11d", this).replace('.', ',')
    }

    private fun Int.formatPrice(): String {
        return String.format("%6d", this)
    }
}