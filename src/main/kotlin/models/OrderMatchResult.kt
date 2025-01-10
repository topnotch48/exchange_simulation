package io.bitvavo.simulation.models

sealed class OrderMatchResult {
    data class MatchSuccessful(var trades: List<Trade>) : OrderMatchResult()
    data object NoMatch : OrderMatchResult()
    data class MatchFailed(var reason: String) : OrderMatchResult()
}