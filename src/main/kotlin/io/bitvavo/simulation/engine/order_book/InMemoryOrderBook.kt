package io.bitvavo.simulation.engine.order_book

import io.bitvavo.simulation.models.Order
import io.bitvavo.simulation.models.OrderBookSnapshot
import io.bitvavo.simulation.models.OrderSide
import java.util.*

typealias OrderPrice = Int
typealias OrderId = Int

class InMemoryOrderBook : OrderBook {
    private val orderComparator = compareBy<Order> { it.seq }
    private val bidOrders = TreeMap<OrderPrice, TreeSet<Order>>(compareByDescending { it })
    private val askOrders = TreeMap<OrderPrice, TreeSet<Order>>()
    private val orders = HashSet<OrderId>()

    override fun contains(orderId: OrderId): Boolean {
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
        val bids = bidOrders.values.flatMap { it.toList() }
        val asks = askOrders.values.flatMap { it.toList() }
        return OrderBookSnapshot(bids, asks)
    }
}
