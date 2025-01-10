package io.bitvavo.simulation.exchange

import io.bitvavo.simulation.models.OrderBookSnapshot
import io.bitvavo.simulation.models.Trade

interface Exchange : AutoCloseable {
    fun executeCommand(cmd: String)
    val onTrade: ((trades: List<Trade>) -> Unit)?
    val onExit: ((orderBook: OrderBookSnapshot) -> Unit)?
    val onFailure: ((reason: String) -> Unit)?
}