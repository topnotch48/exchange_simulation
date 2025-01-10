package io.bitvavo.simulation.formatters

import io.bitvavo.simulation.models.Trade

object TradeFormatter {
    fun formatTrade(trade: Trade): String {
        return "Traded ${trade.aggressionOrderId}, ${trade.restingOrderId}, ${trade.matchedPrice} ${trade.quantity}"
    }
}