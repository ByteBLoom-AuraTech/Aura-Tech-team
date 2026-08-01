package LogiRouteHub

import java.io.File



fun readLinesFromCsvFile(filePathOnDisk: String): List<String> {
    val targetFile = File(filePathOnDisk)
    if (targetFile.exists()) {
        return targetFile.readLines()
    } else {
        println("Error: File not found at path: $filePathOnDisk")
        return emptyList()
    }
}

fun validateLineCommasCounter(currentLine: String, lineNumber: Int, expectedCommas: Int) :Int {
    var validLines = 0
    val currentLineCommas = countCommasForHeaders(currentLine)
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


fun countCommasForHeaders(text: String): Int {
    var commasCount = 0
    var charIndex = 0
    while (charIndex < text.length) {
        if (text[charIndex] == ',') { commasCount++ }
        charIndex++
    }
    return commasCount
}

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
fun checkIfLineContainsOnlySpaces(singleCsvLineText: String): Boolean {
    for (characterIndex in 0 until singleCsvLineText.length) {
        if (singleCsvLineText[characterIndex] > ' ') {
            return false
        }
    }
    return true
}
fun ClenTheData(allRawCsvLines: List<String>): List<String> {
    val validDataLinesOnly = mutableListOf<String>()
    for (lineIndex in 1 until allRawCsvLines.size) {
        val currentLineText = allRawCsvLines[lineIndex]
        if (currentLineText.isNotEmpty() && !checkIfLineContainsOnlySpaces(currentLineText)) {
            validDataLinesOnly.add(currentLineText)
        }
    }
    return validDataLinesOnly
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
fun checkIfTextIsIntegerNumber(textToCheck: String): Boolean {
    if (textToCheck.isEmpty()) return false

    var startCharacterIndex = 0
    if (textToCheck[0] == '-') {
        if (textToCheck.length == 1) return false
        startCharacterIndex = 1
    }

    for (characterIndex in startCharacterIndex until textToCheck.length) {
        val currentCharacter = textToCheck[characterIndex]
        if (currentCharacter < '0' || currentCharacter > '9') {
            return false
        }
    }
    return true
}

fun convertTextToIntegerOrDefault(textToConvert: String): Int {
    val cleanedText = trimSpacesFromEdges(textToConvert)
    if (checkIfTextIsIntegerNumber(cleanedText)) {
        return cleanedText.toInt()
    }
    return -1
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

fun checkIfFleetDataIsValid(columnValuesList: List<String>): Boolean {
    if (columnValuesList.size < 4) return false
    if (columnValuesList[0].isEmpty()) return false
    return true
}

fun buildPackageFromColumns(columnValuesList: List<String>): PackageRaw {
    val packageIdText = columnValuesList[0]
    val packageWeightValue = convertTextToDecimalNumberOrDefault(columnValuesList[1])
    val destinationHubIdText = columnValuesList[2]
    val packagePriorityEnum = Priority.fromString(columnValuesList[3])


    return PackageRaw(packageIdText, packageWeightValue,destinationHubIdText, packagePriorityEnum)
}

fun buildWarehouseFromColumns(columnValuesList: List<String>): WarehouseRaw {
    val warehouseIdText = columnValuesList[0]
    val warehouseNameText = columnValuesList[1]
    val warehouseregionalZoneText = columnValuesList[2]

    return WarehouseRaw(warehouseIdText, warehouseNameText, warehouseregionalZoneText)
}

fun buildRouteFromColumns(columnValuesList: List<String>): RouteRaw {
    val routeIdText = columnValuesList[0]
    val originHubIdText = columnValuesList[1]
    val destinationHubIdText = columnValuesList[2]
    val distanceKmValue = convertTextToDecimalNumberOrDefault(columnValuesList[3])
    val typicalDelayMinValue = convertTextToIntegerOrDefault(columnValuesList[4])

    return RouteRaw(routeIdText, originHubIdText, destinationHubIdText, distanceKmValue, typicalDelayMinValue)
}

fun buildFleetFromColumns(columnValuesList: List<String>): FleetRaw {
    val vehicleIdText = columnValuesList[0]
    val currentHubIdText = columnValuesList[1]
    val maxCapacityKgValue  = convertTextToDecimalNumberOrDefault(columnValuesList[2])
    val costPerKmValue = convertTextToDecimalNumberOrDefault(columnValuesList[3])

    return FleetRaw(vehicleIdText, currentHubIdText, maxCapacityKgValue, costPerKmValue)
}

fun processCsvFileLinesToEntities(csvFileLines:List<String>, entityTypeToProcess: String): List<Any> {
    val createdEntitiesList = mutableListOf<Any>()
    val cleanedCsvDataLinesOnly = ClenTheData(csvFileLines)

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
