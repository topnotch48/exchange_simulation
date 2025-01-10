package io.bitvavo.simulation.engine.order_book

import io.bitvavo.simulation.models.Order
import io.bitvavo.simulation.models.OrderSide
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class InMemoryOrderBookTest {
    @Test
    fun `test adding a new order`() {
        val orderBook = InMemoryOrderBook()
        val order = Order(1, OrderSide.Buy, 100, 1000, System.currentTimeMillis())
        assertTrue(orderBook.add(order), "Order should be added successfully")
        assertTrue(orderBook.contains(order.id), "OrderBook should contain the order")
    }

    @Test
    fun `test adding a duplicate order`() {

    }

    @Test
    fun `test retrieving top bid`() {

    }

    @Test
    fun `test retrieving top ask`() {

    }

    @Test
    fun `test order book snapshot`() {

    }

    @Test
    fun `test empty order book`() {

    }

    @Test
    fun `test orders with the same price but different timestamps`() {

    }
}
