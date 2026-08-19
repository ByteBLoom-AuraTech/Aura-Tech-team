package data.processing.parser

import data.dataholders.WarehouseRaw
import data.processing.validation.*

private const val WAREHOUSE_ID_INDEX = 0
private const val WAREHOUSE_NAME_INDEX = 1
private const val REGIONAL_ZONE_INDEX = 2
private const val LATITUDE_INDEX = 3
private const val LONGITUDE_INDEX = 4

fun parseWarehouses(lines: List<String>): List<WarehouseRaw> {
    if (lines.isEmpty()) {
        return emptyList()
    }

    val expectedColumnCount = getExpectedColumnCount(lines.first())
    val warehouses = mutableListOf<WarehouseRaw>()

    for (line in skipHeader(lines)) {
        if (line.isBlank()) {
            continue
        }

        val columns = extractCleanColumns(line)

        if (!isValidWarehouseRaw(columns, expectedColumnCount, line)) {
            continue
        }

        warehouses.add(
            WarehouseRaw(
                id = columns[WAREHOUSE_ID_INDEX],
                name = columns[WAREHOUSE_NAME_INDEX],
                regionalZone = columns[REGIONAL_ZONE_INDEX],
                latitude = parseCoordinate(columns[LATITUDE_INDEX]),
                longitude = parseCoordinate(columns[LONGITUDE_INDEX])
            )
        )
    }

    return warehouses
}

private fun isValidWarehouseRaw(
    columns: List<String>,
    expectedColumnCount: Int,
    originalLine: String
): Boolean {
    if (!hasExpectedColumnCount(columns, expectedColumnCount)) {
        println("Warning: Invalid column count -> $originalLine")
        return false
    }

    if (!hasRequiredWarehouseFields(columns)) {
        println("Warning: Missing warehouse ID, name, or zone -> $originalLine")
        return false
    }

    return true
}

private fun hasRequiredWarehouseFields(columns: List<String>): Boolean {
    return isNotBlank(columns[WAREHOUSE_ID_INDEX]) &&
            isNotBlank(columns[WAREHOUSE_NAME_INDEX]) &&
            isNotBlank(columns[REGIONAL_ZONE_INDEX])
}

private fun parseCoordinate(value: String): Double {
    if (value.trim().equals("N/A", ignoreCase = true)) {
        return 0.0
    }

    return value.trim().toDoubleOrNull() ?: 0.0
}