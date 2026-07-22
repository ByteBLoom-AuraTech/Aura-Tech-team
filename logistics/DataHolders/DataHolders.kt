package logistics.DataHolders
import logistics.checkDataValidation
import logistics.countCommas
import logistics.readFile
import java.io.File


enum class Priority(val rank: Int) {
    URGENT(3),
    STANDARD(2),
    LOW(1);

    companion object {
        fun fromString(raw: String): Priority {
            return when (raw.trim().uppercase()) {
                "URGENT" -> URGENT
                "STANDARD" -> STANDARD
                "LOW" -> LOW
                else -> LOW
            }
        }
    }
}

data class FleetRaw(
    val vehicleId: String,
    val currentHubId: String,
    val maxCapacityKg: Double,
    val costPerKm: Double
)

data class PackageRaw(
    val id: String,
    val weight: Double,
    val destinationHubId: String,
    val priority: String
)

data class RouteRaw(
    val routeId: String,
    val originHubId: String,
    val destinationHubId: String,
    val distanceKm: Double,
    val typicalDelayMin: Double
)

data class WarehouseRaw(
    val id: String,
    val name: String,
    val regionalZone: String
)
val files = arrayOf("resources/fleet.csv", "resources/packages.csv", "resources/routes.csv", "resources/warehouses.csv")

fun main() {
    var fileIndex = 0
    while (fileIndex < files.size) {
        val currentFile = files[fileIndex]
        println("===========================================")
        println("------------------ $currentFile ------------------")
        println("===========================================")
        readFile(currentFile)
        fileIndex++
    }
}

fun readFile(filePath: String){
    val file = File(filePath)
    if (!file.exists()) return
    val fileContent = file.readText()
    val startIndex = skipHeader(fileContent)
    processDataLines(fileContent, startIndex)
}

fun skipHeader(fileContent: String): Int {
    var fileIndex = 0
    var headerLine = ""
    while (fileIndex < fileContent.length && fileContent[fileIndex] != '\n' && fileContent[fileIndex] != '\r') {
        headerLine = headerLine + fileContent[fileIndex]
        fileIndex++
    }
    return skipLineBreaks(fileContent, fileIndex)
}

fun skipLineBreaks(fileContent: String , fileIndex: Int): Int {
    var fileIndex = fileIndex
    while (fileIndex < fileContent.length && (fileContent[fileIndex] == '\n' || fileContent[fileIndex] == '\r')) {
        fileIndex++
    }
    return fileIndex
}

fun extractNextLine (fileContent: String , fileIndex: Int): Pair<String, Int> {
    var currentLine = ""
    var fileIndex = fileIndex
    while (fileIndex < fileContent.length && fileContent[fileIndex] != '\n' && fileContent[fileIndex] != '\r') {
        currentLine = currentLine + fileContent[fileIndex]
        fileIndex++
    }
    val nextIndex = skipLineBreaks(fileContent, fileIndex)
    return Pair(currentLine, nextIndex)
}

fun validateLineCommas(currentLine: String , lineNumber: Int , expectedCommas: Int){
    val currentLineCommas = countCommas(currentLine)
    if (currentLineCommas != expectedCommas) {
        if (currentLineCommas < expectedCommas) {
            println("WARNING--- : in line number $lineNumber there is a deleted value")
        } else {
            println("WARNING--- : in line number $lineNumber there is an extra value")
        }
    } else {
        checkDataValidation(currentLine, lineNumber)
    }
}

fun processDataLines(fileContent: String , fileIndex: Int){
    var lineNumber = 1
    var currentIndex = fileIndex
    while (currentIndex < fileContent.length) {
        lineNumber++
        val (currentLine, nextIndex) = extractNextLine(fileContent, currentIndex)
        currentIndex = nextIndex

        if (currentLine.isNotEmpty()) {
            val cleanLine = removeSpaces(currentLine)
            validateLineCommas(cleanLine, lineNumber, 3)
            processLineValues(cleanLine)
        }
    }
}

fun removeSpaces(line: String): String {
    var cleanLineWithoutSpaces = ""
    var lineIndex = 0
    while (lineIndex < line.length) {
        if (line[lineIndex] != ' ') {
            cleanLineWithoutSpaces = cleanLineWithoutSpaces + line[lineIndex]
        }
        lineIndex++
    }
    return cleanLineWithoutSpaces
}

fun isValidNumber(currentWord: String): Boolean {
    var wordCharIndex = 0
    var decimalDotsCount = 0
    var isNumericValue = true
    while (wordCharIndex < currentWord.length) {
        if (currentWord[wordCharIndex] == '.') {
            decimalDotsCount++
        } else if (currentWord[wordCharIndex] < '0' || currentWord[wordCharIndex] > '9') {
            isNumericValue = false
        }
        wordCharIndex++
    }
    return isNumericValue && decimalDotsCount <= 1
}

fun processLineValues(cleanLine: String){
    var currentWord = ""
    var cleanLineIndex = 0
    while (cleanLineIndex <= cleanLine.length) {
        if (cleanLineIndex < cleanLine.length && cleanLine[cleanLineIndex] != ',') {
            currentWord = currentWord + cleanLine[cleanLineIndex]
        } else {
            if (currentWord.isNotEmpty()) {
                isValidNumber(currentWord)
            }
            currentWord = ""
        }
        cleanLineIndex++
    }
}

fun countCommas(text: String): Int {
    var commasCount = 0
    var charIndex = 0
    while (charIndex < text.length) {
        if (text[charIndex] == ',') { commasCount++ }
        charIndex++
    }
    return commasCount
}