package io.bitvavo.simulation.extensions

import java.io.Writer

object StreamWriter {
    fun Writer.writeLine(line: String) {
        this.write(line)
        this.write(System.lineSeparator())
    }
}