package io.bitvavo.simulation

import io.bitvavo.simulation.engine.InMemoryMatchingEngine
import io.bitvavo.simulation.engine.order_book.InMemoryOrderBook
import io.bitvavo.simulation.exchange.SimulatorExchange
import io.bitvavo.simulation.exchange.sequence_generator.CounterGenerator
import io.bitvavo.simulation.extensions.StreamWriter.writeLine
import io.bitvavo.simulation.formatters.OrderBookFormatter.formatOrderBook
import io.bitvavo.simulation.formatters.TradeFormatter.formatTrade
import java.io.*

fun main() {
    val input = BufferedReader(InputStreamReader(System.`in`))
    val output = PrintWriter(OutputStreamWriter(System.out))
    val logs = PrintWriter(FileWriter("logs.info", true))

    try {
        val simulator = SimulatorExchange(
            engine = InMemoryMatchingEngine(InMemoryOrderBook()),
            generator = CounterGenerator()
        )

        simulator.onTrade = { trades -> trades.forEach { output.writeLine(formatTrade(it)) } }
        simulator.onFailure = { logs.writeLine(it) }
        simulator.onSnapshot = { output.writeLine(formatOrderBook(it)) }
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