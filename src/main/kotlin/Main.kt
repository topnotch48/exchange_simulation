package io.bitvavo.simulation

import io.bitvavo.simulation.engine.InMemoryMatchingEngine
import io.bitvavo.simulation.engine.order_book.InMemoryOrderBook
import io.bitvavo.simulation.exchange.SimulatorExchange
import io.bitvavo.simulation.exchange.clock.Clock
import io.bitvavo.simulation.extensions.StreamWriter.writeLine
import io.bitvavo.simulation.formatters.OrderBookFormatter.formatOrderBook
import io.bitvavo.simulation.formatters.TradeFormatter.formatTrade
import java.io.*

fun main() {
    val input = BufferedReader(InputStreamReader(System.`in`))
    val output = BufferedWriter(OutputStreamWriter(System.out))
    val logs = BufferedWriter(FileWriter("logs.info", true))

    try {
        val simulator = SimulatorExchange(
            engine = InMemoryMatchingEngine(InMemoryOrderBook()),
            clock = object : Clock { override fun currentTimeMillis() = System.currentTimeMillis() }
        )

        simulator.onTrade = { trades -> trades.forEach { output.writeLine(formatTrade(it)) } }
        simulator.onFailure = { logs.writeLine(it) }
        simulator.onExit = { output.writeLine(formatOrderBook(it)) }
        simulator.use {
            while (true) {
                try {
                    val command = input.readLine() ?: break
                    simulator.executeCommand(command)
                }
                catch (ex: Exception) {
                    logs.writeLine("Unhandled error occurred: ${ex.message}")
                }
                finally {
                    output.flush()
                }
            }
        }
    }
    finally {
        listOf(input, output, logs).forEach { stream ->
            try {
                stream.close()
            } catch (e: IOException) {
                println("Failed to close $stream: ${e.message}")
            }
        }
    }
}