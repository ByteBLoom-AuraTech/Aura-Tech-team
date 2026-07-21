package logistics.models
import logistics.checkDataValidation
import logistics.countCommas
import logistics.readFile
import java.io.File


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


fun readFile(filePath: String) {
    val fileContent = File(filePath).readText()

    var fileIndex = 0
    var headerLine = ""

    while (fileIndex < fileContent.length && fileContent[fileIndex] != '\n' && fileContent[fileIndex] != '\r') {
        headerLine = headerLine + fileContent[fileIndex]
        fileIndex++
    }

    val expectedCommas = countCommas(headerLine)

    while (fileIndex < fileContent.length && (fileContent[fileIndex] == '\n' || fileContent[fileIndex] == '\r')) {
        fileIndex++
    }

    var lineNumber = 1

    while (fileIndex < fileContent.length) {
        lineNumber++
        var currentLine = ""

        while (fileIndex < fileContent.length && fileContent[fileIndex] != '\n' && fileContent[fileIndex] != '\r') {
            currentLine = currentLine + fileContent[fileIndex]
            fileIndex++
        }

        while (fileIndex < fileContent.length && (fileContent[fileIndex] == '\n' || fileContent[fileIndex] == '\r')) {
            fileIndex++
        }

        if (currentLine.length == 0) {
            continue
        }

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
}


fun checkDataValidation(line: String, lineNumber: Int) {
    var cleanLineWithoutSpaces = ""
    var lineIndex = 0

    while (lineIndex < line.length) {
        if (line[lineIndex] != ' ') {
            cleanLineWithoutSpaces = cleanLineWithoutSpaces + line[lineIndex]
        }
        lineIndex++
    }

    var currentWord = ""
    var cleanLineIndex = 0

    while (cleanLineIndex <= cleanLineWithoutSpaces.length) {
        if (cleanLineIndex < cleanLineWithoutSpaces.length && cleanLineWithoutSpaces[cleanLineIndex] != ',') {
            currentWord = currentWord + cleanLineWithoutSpaces[cleanLineIndex]
        } else {
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

            if (isNumericValue == false || decimalDotsCount > 1 || currentWord.length == 0) {
                println("WARNING--- : in line number $lineNumber malformed numeric data!")
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
        if (text[charIndex] == ',') {
            commasCount++
        }
        charIndex++
    }
    return commasCount
}