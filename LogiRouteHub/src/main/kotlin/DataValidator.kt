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
    if (columnValuesList.size != 4) {
        return false
    }
    if (columnValuesList[0].isEmpty()) return false
    if (columnValuesList[2].isEmpty()) return false
    return true
}

fun checkIfWarehouseDataIsValid(columnValuesList: List<String>): Boolean {
    if (columnValuesList.size != 3) {
        return false
    }
    if (columnValuesList[0].isEmpty()) return false
    return true
}

fun checkIfRouteDataIsValid(columnValuesList: List<String>): Boolean {
    if (columnValuesList.size != 5) {
        return false
    }
    if (columnValuesList[0].isEmpty()) return false
    return true
}

fun checkIfFleetDataIsValid(columnValuesList: List<String>): Boolean {
    if (columnValuesList.size != 4) {
        return false
    }
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

fun processPackageLine(lineText: String, createdEntitiesList: MutableList<Any>, lineIndex: Int): Boolean {
    val extractedColumnValuesList = splitCsvLineByComma(lineText)
    if (checkIfPackageDataIsValid(extractedColumnValuesList)) {
        createdEntitiesList.add(buildPackageFromColumns(extractedColumnValuesList))
        return true
    }
    println("Warning: package row has missing required fields: $lineText")
    return false
}

fun processWarehouseLine(lineText: String, createdEntitiesList: MutableList<Any>, lineIndex: Int): Boolean {
    val extractedColumnValuesList = splitCsvLineByComma(lineText)
    if (checkIfWarehouseDataIsValid(extractedColumnValuesList)) {
        createdEntitiesList.add(buildWarehouseFromColumns(extractedColumnValuesList))
        return true
    }
    println("Warning: warehouse row has missing required fields: $lineText")
    return false
}

fun processRouteLine(lineText: String, createdEntitiesList: MutableList<Any>, lineIndex: Int): Boolean {
    val extractedColumnValuesList = splitCsvLineByComma(lineText)
    if (checkIfRouteDataIsValid(extractedColumnValuesList)) {
        createdEntitiesList.add(buildRouteFromColumns(extractedColumnValuesList))
        return true
    }
    println("Warning: route row has missing required fields: $lineText")
    return false
}

fun processFleetLine(lineText: String, createdEntitiesList: MutableList<Any>, lineIndex: Int): Boolean {
    val extractedColumnValuesList = splitCsvLineByComma(lineText)
    if (checkIfFleetDataIsValid(extractedColumnValuesList)) {
        createdEntitiesList.add(buildFleetFromColumns(extractedColumnValuesList))
        return true
    }
    println("Warning: fleet row has missing required fields: $lineText")
    return false
}

fun processCsvFileLinesToEntities(csvFileLines: List<String>, entityTypeToProcess: String): List<Any> {
    val createdEntitiesList = mutableListOf<Any>()
    val cleanedCsvDataLinesOnly = ClenTheData(csvFileLines)

    for (lineIndex in 0 until cleanedCsvDataLinesOnly.size) {
        val singleLineText = cleanedCsvDataLinesOnly[lineIndex]
        when (entityTypeToProcess) {
            "PACKAGE" -> processPackageLine(singleLineText, createdEntitiesList, lineIndex)
            "WAREHOUSE" -> processWarehouseLine(singleLineText, createdEntitiesList, lineIndex)
            "ROUTE" -> processRouteLine(singleLineText, createdEntitiesList, lineIndex)
            "FLEET" -> processFleetLine(singleLineText, createdEntitiesList, lineIndex)
        }
    }
    return createdEntitiesList
}