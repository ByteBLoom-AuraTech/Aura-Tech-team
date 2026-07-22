
package logistics
import java.io.File
// =========================================================================
// 1. YOUR MANUAL CORE TOOLS (DO NOT TOUCH - USED FOR ALL FILES)
// =========================================================================

fun manualTrim(input: String): String {
    var start = 0
    var end = input.length - 1
    while (start <= end && input[start] <= ' ') start++
    while (end >= start && input[end] <= ' ') end--
    return input.substring(start, end + 1)
}


fun manualSplitCsv(line: String): List<String> {
    val result = mutableListOf<String>()
    var currentColumn = ""
    for (i in 0 until line.length) {
        val char = line[i]
        if (char == ',') {
            result.add(manualTrim(currentColumn))
            currentColumn = ""
        } else {
            currentColumn += char
        }
    }
    result.add(manualTrim(currentColumn))
    return result
}

fun manualParseDouble(input: String): Double {
    return try {
        java.lang.Double.parseDouble(input)
    } catch (e: Exception) {
        -1.0
    }
}

fun filterRawLinesManually(lines: List<String>): List<String> {
    val cleanLines = mutableListOf<String>()
    for (i in 1 until lines.size) { // Skip header row at index 0
        val currentLine = lines[i]
        if (currentLine.isNotEmpty() && !isLineOnlySpaces(currentLine)) {
            cleanLines.add(currentLine)
        }
    }
    return cleanLines
}

fun isLineOnlySpaces(line: String): Boolean {
    for (i in 0 until line.length) {
        if (line[i] > ' ') return false
    }
    return true
}






















fun readCsvFile(filePath: String): List<String> {
    return try {
        File(filePath).readLines()
    } catch (e: Exception) {
        println("Error reading file at $filePath: ${e.message}")
        emptyList()
    }
}