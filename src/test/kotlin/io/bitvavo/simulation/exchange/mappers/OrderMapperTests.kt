package io.bitvavo.simulation.exchange.mappers

import io.bitvavo.simulation.exchange.mappers.OrderMapper.tryParseOrder
import io.bitvavo.simulation.models.Order
import io.bitvavo.simulation.models.OrderSide
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource


class OrderMapperTests {
    @ParameterizedTest
    @MethodSource("validTestCases")
    fun `test success orders`(command: String, expected: Order) {
        val result = command.tryParseOrder(0)
        assertTrue(result.isSuccess)
        val order = result.getOrNull()!!
        assertEquals(expected, order)
    }

    @ParameterizedTest
    @MethodSource("invalidTestCases")
    fun `test invalid orders`(command: String, failureMessage: String) {
        val result = command.tryParseOrder(0)
        assertTrue(result.isFailure)
        val error = result.exceptionOrNull()!!
        assertEquals(error.message, failureMessage)
    }

    companion object {
        @JvmStatic
        fun validTestCases(): List<Array<Any>>  = listOf(
            arrayOf("10000,B,98,25500", Order(10000, OrderSide.Buy, 98, 25500, 0)),
            arrayOf("9000,S,999999,25500", Order(9000, OrderSide.Sell, 999999, 25500, 0)),
            arrayOf("3333,B,100,999999999", Order(3333, OrderSide.Buy, 100, 999999999, 0)),
            arrayOf("10000999,S,100,999999999", Order(10000999, OrderSide.Sell, 100, 999999999, 0))
        )

        @JvmStatic
        fun invalidTestCases(): List<Array<Any>>  = listOf(
            arrayOf("123123-99999", "Invalid order format: '123123-99999'"),
            arrayOf("$$$-##1-99999", "Invalid order format: '$$$-##1-99999'"),
            arrayOf("1111111111111111,B,98,25500", "Invalid order ID: '1111111111111111'"),
            arrayOf("-9000,B,98,25500", "Invalid order ID: '-9000'"),
            arrayOf("AAA,B,98,25500", "Invalid order ID: 'AAA'"),
            arrayOf("1000,Y,98,25500", "Invalid order side: 'Y'"),
            arrayOf("1000,AAA#,98,25500", "Invalid order side: 'AAA#'"),
            arrayOf("1000,S,-100,25500", "Invalid order price: '-100'"),
            arrayOf("1000,S,0,25500", "Invalid order price: '0'"),
            arrayOf("1000,S,100,0", "Invalid order quantity: '0'"),
            arrayOf("1000,S,100,-100", "Invalid order quantity: '-100'"),
        )
    }
}