
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

// ==================================================================
// 2. FILE 1: Packeges PIPELINE (UPDATED WITH NOOR'S DATA MODELS)
// ======================================================

fun validateAndParsePackage(rawLine: String): Package? {
    val columns = manualSplitCsv(rawLine)
    if (columns.size < 4 || columns[3].isEmpty()) {
        println("Warning: Skipping malformed package row.")
        return null
    }
    val id = columns[0]
    val weight = manualParseDouble(columns[1])
    // Using Noor's official Priority enum and conversion tool
    val priority = Priority.fromString(columns[2])
    val destinationHubId = columns[3]

    return Package(id, weight, priority, destinationHubId)
}

fun processPackageCsv(allLines: List<String>): List<Package> {
    val cleanParsedPackages = mutableListOf<Package>()
    val filteredLines = filterRawLinesManually(allLines)
    for (i in 0 until filteredLines.size) {
        val parsedObject = validateAndParsePackage(filteredLines[i])
        if (parsedObject != null) cleanParsedPackages.add(parsedObject)
    }
    return cleanParsedPackages
}

// =========================================================================
// 3. FILE 2: WAREHOUSES PIPELINE
// =========================================================================

fun validateAndParseWarehouse(rawLine: String): Warehouse? {
    val columns = manualSplitCsv(rawLine)
    if (columns.size < 4 || columns[0].isEmpty()) {
        println("Warning: Skipping malformed warehouse row.")
        return null
    }
    val warehouseId = columns[0]
    val name = columns[1]
    val capacity = manualParseDouble(columns[2])
    val location = columns[3]

    return Warehouse(warehouseId, name, capacity, location)
}

fun processWarehouseCsv(allLines: List<String>): List<Warehouse> {
    val cleanParsedWarehouses = mutableListOf<Warehouse>()
    val filteredLines = filterRawLinesManually(allLines)
    for (i in 0 until filteredLines.size) {
        val parsedObject = validateAndParseWarehouse(filteredLines[i])
        if (parsedObject != null) cleanParsedWarehouses.add(parsedObject)
    }
    return cleanParsedWarehouses
}

// =========================================================================
// 4. FILE 3: ROUTES PIPELINE
// =========================================================================

fun validateAndParseRoute(rawLine: String): RouteRecord? {
    val columns = manualSplitCsv(rawLine)
    if (columns.size < 4 || columns[0].isEmpty()) {
        println("Warning: Skipping malformed route row.")
        return null
    }
    val routeId = columns[0]
    val originId = columns[1]
    val destinationId = columns[2]
    val distance = manualParseDouble(columns[3])

    return RouteRecord(routeId, originId, destinationId, distance)
}

fun processRouteCsv(allLines: List<String>): List<RouteRecord> {
    val cleanParsedRoutes = mutableListOf<RouteRecord>()
    val filteredLines = filterRawLinesManually(allLines)
    for (i in 0 until filteredLines.size) {
        val parsedObject = validateAndParseRoute(filteredLines[i])
        if (parsedObject != null) cleanParsedRoutes.add(parsedObject)
    }
    return cleanParsedRoutes
}

// =========================================================================
// 5. FILE 4: FLEET (VEHICLES) PIPELINE
// =========================================================================

fun validateAndParseVehicle(rawLine: String): Vehicle? {
    val columns = manualSplitCsv(rawLine)
    if (columns.size < 4 || columns[0].isEmpty()) {
        println("Warning: Skipping malformed vehicle row.")
        return null
    }
    val vehicleId = columns[0]
    val type = columns[1]
    val capacity = manualParseDouble(columns[2])
    val status = columns[3]

    return Vehicle(vehicleId, type, capacity, status)
}

fun processVehicleCsv(allLines: List<String>): List<Vehicle> {
    val cleanParsedVehicles = mutableListOf<Vehicle>()
    val filteredLines = filterRawLinesManually(allLines)
    for (i in 0 until filteredLines.size) {
        val parsedObject = validateAndParseVehicle(filteredLines[i])
        if (parsedObject != null) cleanParsedVehicles.add(parsedObject)
    }
    return cleanParsedVehicles
}


fun main() {
    println("--- Starting DataValidator Test System for LogiRoute Genesis ---")

    // 1. Testing Packages File (Dummy)
    val dummyPackageLines = listOf(
        "packageId,weight,priority,destinationHubId",
        "PKG-001, 45.5, URGENT, HUB-WEST ",
        "PKG-002, invalid_weight, STANDARD, HUB-EAST",
        "PKG-003, 12.0"
    )
    val parsedPackages = processPackageCsv(dummyPackageLines)
    println(" Successfully Extracted Packages (${parsedPackages.size}):")
    parsedPackages.forEach { println("   $it") }
    println("=====================================================")

    // 2. Testing Warehouses File
    val dummyWarehouseLines = listOf(
        "warehouseId,name,capacity,location",
        "HUB-WEST, West Coast Hub, 5000.0, California",
        "HUB-EAST, East Coast Hub, abc, New York",
        ", Missing ID Hub, 1000.0, Texas"
    )
    val parsedWarehouses = processWarehouseCsv(dummyWarehouseLines)
    println(" Successfully Extracted Warehouses (${parsedWarehouses.size}):")
    parsedWarehouses.forEach { println("   $it") }
    println("==============================================")

    // 3. Testing Routes File
    val dummyRouteLines = listOf(
        "routeId,originId,destinationId,distance",
        "R-01, HUB-WEST, HUB-EAST, 2800.5",
        "R-02, HUB-EAST, HUB-SOUTH, 1200.0"
    )
    val parsedRoutes = processRouteCsv(dummyRouteLines)
    println(" Successfully Extracted Routes (${parsedRoutes.size}):")
    parsedRoutes.forEach { println("   $it") }
    println("=======================================")

    // هنا مكان الكود الحقيقي الجديد لفحص ملف الشحنات الفعلي (مع إضافة الطباعة):
    println("\n--- Testing Real CSV File Reading ---")
    val realLines = readCsvFile("src/main/resources/packages.csv")
    val realPackages = processPackageCsv(realLines)
    println(" Successfully Extracted Real Packages (${realPackages.size}):")
    realPackages.forEach { println("   $it") }
    println("=====================================================")

    // 4. Testing Fleet / Vehicles File
    val dummyVehicleLines = listOf(
        "vehicleId,type,capacity,status",
        "V-101, Truck, 15.0, ACTIVE",
        "V-102, Van, 3.5, MAINTENANCE"
    )
    val parsedVehicles = processVehicleCsv(dummyVehicleLines)
    println("Successfully Extracted Vehicles (${parsedVehicles.size}):")
    parsedVehicles.forEach { println("   $it") }
    println("===== Diagnostic Test Completed Successfully! =====")
}


fun readCsvFile(filePath: String): List<String> {
    return try {
        File(filePath).readLines()
    } catch (e: Exception) {
        println("Error reading file at $filePath: ${e.message}")
        emptyList()
    }
}