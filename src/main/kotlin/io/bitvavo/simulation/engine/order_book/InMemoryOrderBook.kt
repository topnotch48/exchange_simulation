package io.bitvavo.simulation.engine.order_book

import io.bitvavo.simulation.models.Order
import io.bitvavo.simulation.models.OrderBookSnapshot
import io.bitvavo.simulation.models.OrderSide
import java.util.*

class InMemoryOrderBook : OrderBook {
    private val orderComparator = compareBy<Order> { it.timestampMs }
    private val bidOrders = TreeMap<Int, TreeSet<Order>>(compareByDescending { it })
    private val askOrders = TreeMap<Int, TreeSet<Order>>()
    private val orders = HashSet<Int>()

    override fun contains(orderId: Int): Boolean {
        return orders.contains(orderId)
    }

    override fun add(order: Order): Boolean {
        if (contains(order.id)) {
            return false
        }

        val orderSet = when (order.side) {
            OrderSide.Buy -> bidOrders.computeIfAbsent(order.price) { TreeSet(orderComparator) }
            OrderSide.Sell -> askOrders.computeIfAbsent(order.price) { TreeSet(orderComparator) }
        }

        orderSet.add(order)
        orders.add(order.id)
        return true
    }

    override fun getTopBid(): Order? {
        val topEntry = bidOrders.firstEntry() ?: return null
        val topOrder = topEntry.value.firstOrNull() ?: return null
        topEntry.value.remove(topOrder)
        if (topEntry.value.isEmpty()) {
            bidOrders.remove(topEntry.key)
        }
        orders.remove(topOrder.id)
        return topOrder
    }

    override fun getTopAsk(): Order? {
        val topEntry = askOrders.firstEntry() ?: return null
        val topOrder = topEntry.value.firstOrNull() ?: return null
        topEntry.value.remove(topOrder)
        if (topEntry.value.isEmpty()) {
            askOrders.remove(topEntry.key)
        }
        orders.remove(topOrder.id)
        return topOrder
    }

    override fun getOrderBookSnapshot(): OrderBookSnapshot {
        val bids = bidOrders.flatMap { (_, orders) -> orders }
        val asks = askOrders.flatMap { (_, orders) -> orders }
        return OrderBookSnapshot(bids, asks)
    }
}
