
package logistics

import java.io.File



//NOOR

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

fun validateLineCommasCounter(currentLine: String, lineNumber: Int, expectedCommas: Int) :Int {
    val currentLineCommas = countCommasForHeaders(currentLine)
    var validLines = 0
    if (currentLineCommas != expectedCommas) {
        if (currentLineCommas < expectedCommas) {
            println("WARNING--- : in line number $lineNumber there is a deleted value")
        } else {
            println("WARNING--- : in line number $lineNumber there is an extra value")
        }
    } else {
        println("Line $lineNumber comma structure is valid.")
        validLines++
    }
    return validLines
}

fun processDataLines(fileContent: String , fileIndex: Int){
    var lineNumber = 1
    var currentIndex = fileIndex
    while (currentIndex < fileContent.length) {
        lineNumber++
        val (currentLine, nextIndex) = extractNextLine(fileContent, currentIndex)
        currentIndex = nextIndex

        if (currentLine.isNotEmpty()) {
            val cleanLine = trimSpacesFromEdges(currentLine)
            validateLineCommasCounter(cleanLine, lineNumber, 3)
            splitCsvLineByComma(cleanLine)
        }
    }
}

fun countCommasForHeaders(text: String): Int {
    var commasCount = 0
    var charIndex = 0
    while (charIndex < text.length) {
        if (text[charIndex] == ',') { commasCount++ }
        charIndex++
    }
    return commasCount
}

//=========================================================================================================================================

//BARAA

fun trimSpacesFromEdges(textToClean: String): String {
    var startPointer = 0
    var endPointer = textToClean.length - 1

    while (startPointer <= endPointer && textToClean[startPointer] <= ' ') {
        startPointer++
    }
    while (endPointer >= startPointer && textToClean[endPointer] <= ' ') {
        endPointer--
    }
    return textToClean.substring(startPointer, endPointer + 1)
}

fun splitCsvLineByComma(singleCsvLineText: String): List<String> {
    val extractedColumnsList = mutableListOf<String>()
    var currentColumnTextBuilder = ""

    for (characterIndex in 0 until singleCsvLineText.length) {
        val currentCharacter = singleCsvLineText[characterIndex]
        if (currentCharacter == ',') {
            extractedColumnsList.add(trimSpacesFromEdges(currentColumnTextBuilder))
            currentColumnTextBuilder = ""
        } else {
            currentColumnTextBuilder += currentCharacter
        }
    }
    extractedColumnsList.add(trimSpacesFromEdges(currentColumnTextBuilder))
    return extractedColumnsList
}




fun checkIfTextIsDecimalNumber(textToCheck: String): Boolean {
    if (textToCheck.isEmpty()) return false

    var countOfDecimalPoints = 0
    var startCharacterIndex = 0

    if (textToCheck[0] == '-') {
        if (textToCheck.length == 1) return false
        startCharacterIndex = 1
    }

    for (characterIndex in startCharacterIndex until textToCheck.length) {
        val currentCharacter = textToCheck[characterIndex]
        if (currentCharacter == '.') {
            countOfDecimalPoints++
            if (countOfDecimalPoints > 1) return false
        } else if (currentCharacter < '0' || currentCharacter > '9') {
            return false
        }
    }
    return true
}

fun convertTextToDecimalNumberOrDefault(textToConvert: String): Double {
    val cleanedText = trimSpacesFromEdges(textToConvert)
    if (checkIfTextIsDecimalNumber(cleanedText)) {
        return cleanedText.toDouble()
    }
    return -1.0
}


fun checkIfPackageDataIsValid(columnValuesList: List<String>): Boolean {
    if (columnValuesList.size < 4) return false
    if (columnValuesList[3].isEmpty()) return false
    return true
}

fun checkIfWarehouseDataIsValid(columnValuesList: List<String>): Boolean {
    if (columnValuesList.size < 4) return false
    if (columnValuesList[0].isEmpty()) return false
    return true
}

fun checkIfRouteDataIsValid(columnValuesList: List<String>): Boolean {
    if (columnValuesList.size < 4) return false
    if (columnValuesList[0].isEmpty()) return false
    return true
}

fun checkIfVehicleDataIsValid(columnValuesList: List<String>): Boolean {
    if (columnValuesList.size < 4) return false
    if (columnValuesList[0].isEmpty()) return false
    return true
}

fun buildPackageFromColumns(columnValuesList: List<String>): Package {
    val packageIdText = columnValuesList[0]
    val packageWeightValue = convertTextToDecimalNumberOrDefault(columnValuesList[1])
    val destinationHubIdText = columnValuesList[2]
    val packagePriorityEnum = Priority.fromString(columnValuesList[3])


    return Package(packageIdText, packageWeightValue,destinationHubIdText, packagePriorityEnum)
}

fun buildWarehouseFromColumns(columnValuesList: List<String>): Warehouse {
    val warehouseIdText = columnValuesList[0]
    val warehouseNameText = columnValuesList[1]
    val warehouseregionalZoneText = columnValuesList[2]

    return Warehouse(warehouseIdText, warehouseNameText, warehouseregionalZoneText)
}

fun buildRouteFromColumns(columnValuesList: List<String>): RouteRecord {
    val routeIdText = columnValuesList[0]
    val originHubIdText = columnValuesList[1]
    val destinationHubIdText = columnValuesList[2]
    val distanceKmValue = convertTextToDecimalNumberOrDefault(columnValuesList[3])
    val typicalDelayMin = convertTextToDecimalNumberOrDefault(columnValuesList[4])

    return RouteRecord(routeIdText, originHubIdText, destinationHubIdText, distanceKmValue,typicalDelayMin)
}

fun buildFleetFromColumns(columnValuesList: List<String>): Fleet {
    val vehicleIdText = columnValuesList[0]
    val currentHubIdText = columnValuesList[1]
    val maxCapacityKgValue  = convertTextToDecimalNumberOrDefault(columnValuesList[2])
    val costPerKmValue = convertTextToDecimalNumberOrDefault(columnValuesList[3])

    return Fleet(vehicleIdText, currentHubIdText, maxCapacityKgValue, costPerKmValue)
}

fun processCsvFileLinesToEntities(csvFileLines: List<String>, entityTypeToProcess: String): List<Any> {
    val createdEntitiesList = mutableListOf<Any>()
    val cleanedCsvDataLinesOnly = removeHeaderAndEmptyLines(csvFileLines)

    for (lineIndex in 0 until cleanedCsvDataLinesOnly.size) {
        val singleLineText = cleanedCsvDataLinesOnly[lineIndex]
        val extractedColumnValuesList = splitCsvLineByComma(singleLineText)

        when (entityTypeToProcess) {
            "PACKAGE" -> {
                if (checkIfPackageDataIsValid(extractedColumnValuesList)) {
                    val createdPackageEntity = buildPackageFromColumns(extractedColumnValuesList)
                    createdEntitiesList.add(createdPackageEntity)
                } else {
                    println("Warning: Skipping malformed package row at index $lineIndex.")
                }
            }
            "WAREHOUSE" -> {
                if (checkIfWarehouseDataIsValid(extractedColumnValuesList)) {
                    val createdWarehouseEntity = buildWarehouseFromColumns(extractedColumnValuesList)
                    createdEntitiesList.add(createdWarehouseEntity)
                } else {
                    println("Warning: Skipping malformed warehouse row at index $lineIndex.")
                }
            }
            "ROUTE" -> {
                if (checkIfRouteDataIsValid(extractedColumnValuesList)) {
                    val createdRouteEntity = buildRouteFromColumns(extractedColumnValuesList)
                    createdEntitiesList.add(createdRouteEntity)
                } else {
                    println("Warning: Skipping malformed route row at index $lineIndex.")
                }
            }
            "FLEET" -> {
                if (checkIfFleetDataIsValid(extractedColumnValuesList)) {
                    val createdFleetEntity = buildFleetFromColumns(extractedColumnValuesList)
                    createdEntitiesList.add(createdFleetEntity)
                } else {
                    println("Warning: Skipping malformed Fleet row at index $lineIndex.")
                }
            }
        }
    }
    return createdEntitiesList
}
