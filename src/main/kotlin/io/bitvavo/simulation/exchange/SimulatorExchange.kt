package io.bitvavo.simulation.exchange

import io.bitvavo.simulation.engine.MatchingEngine
import io.bitvavo.simulation.exchange.clock.Clock
import io.bitvavo.simulation.exchange.mappers.OrderMapper.tryParseOrder
import io.bitvavo.simulation.models.OrderBookSnapshot
import io.bitvavo.simulation.models.OrderMatchResult
import io.bitvavo.simulation.models.Trade

class SimulatorExchange(private val engine: MatchingEngine, private val clock: Clock) : Exchange {
    override var onTrade: ((trades: List<Trade>) -> Unit)? = null
    override var onFailure: ((reason: String) -> Unit)? = null
    override var onExit: ((orderBook: OrderBookSnapshot) -> Unit)? = null

    override fun executeCommand(cmd: String) {
        val timestamp = clock.currentTimeMillis()

        val parsedOrder = cmd.tryParseOrder(timestamp)

        if (parsedOrder.isFailure) {
            val reason = parsedOrder.exceptionOrNull()?.message ?: "order parsing failure."
            onFailure?.invoke(reason)
            return
        }

        val order = parsedOrder.getOrNull()!!

        when (val matchResult = engine.tryMatch(order)) {
            is OrderMatchResult.MatchFailed -> onFailure?.invoke(matchResult.reason)
            is OrderMatchResult.NoMatch -> onFailure?.invoke("no orders matched.")
            is OrderMatchResult.MatchSuccessful -> onTrade?.invoke(matchResult.trades)
        }
    }

    override fun close() {
        onExit?.invoke(engine.getOrderBookSnapshot())
    }
}