package io.bitvavo.simulation.engine

import io.bitvavo.simulation.engine.order_book.OrderBook
import io.bitvavo.simulation.models.*

class InMemoryMatchingEngine(private val orderBook: OrderBook) : MatchingEngine {

    override fun tryMatch(order: Order): OrderMatchResult {
        if (orderBook.contains(order.id)) {
            return OrderMatchResult.MatchFailed("Duplicate order id ${order.id}.")
        }

        val trades = when (order.side) {
            OrderSide.Buy -> matchBuyOrder(order)
            OrderSide.Sell -> matchSellOrder(order)
        }

        if (trades.isEmpty())
            return OrderMatchResult.NoMatch

        return OrderMatchResult.MatchSuccessful(trades)
    }

    override fun getOrderBookSnapshot(): OrderBookSnapshot {
        return orderBook.getOrderBookSnapshot()
    }

    private fun matchBuyOrder(order: Order): List<Trade> {
        val trades = mutableListOf<Trade>()

        while (order.quantity > 0) {
            val topAskOrder = orderBook.getTopAsk()
                ?: break

            if (topAskOrder.price > order.price) {
                orderBook.add(topAskOrder)
                break
            }

            val matchedQuantity = minOf(topAskOrder.quantity, order.quantity)

            val trade = Trade(order.id, topAskOrder.id, topAskOrder.price, matchedQuantity)
            trades.add(trade)

            order.quantity -= matchedQuantity
            topAskOrder.quantity -= matchedQuantity

            if (topAskOrder.quantity > 0)
                orderBook.add(topAskOrder)
        }

        if (order.quantity > 0)
            orderBook.add(order)

        return trades
    }

    private fun matchSellOrder(order: Order): List<Trade>  {
        val trades = mutableListOf<Trade>()

        while (order.quantity > 0) {
            val topBidOrder = orderBook.getTopBid()
                ?: break

            if (topBidOrder.price < order.price) {
                orderBook.add(topBidOrder)
                break
            }

            val matchedQuantity = minOf(order.quantity, topBidOrder.quantity)

            val trade = Trade(topBidOrder.id, order.id, topBidOrder.price, matchedQuantity)
            trades.add(trade)

            order.quantity -= matchedQuantity
            topBidOrder.quantity -= matchedQuantity

            if (topBidOrder.quantity > 0)
                orderBook.add(topBidOrder)
        }

        if (order.quantity > 0)
            orderBook.add(order)

        return trades
    }
}