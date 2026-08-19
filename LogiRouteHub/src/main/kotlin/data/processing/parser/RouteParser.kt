package data.processing.parser

import data.dataholders.RouteRaw
import data.processing.validation.*

private const val ROUTE_ID_INDEX = 0
private const val ORIGIN_HUB_ID_INDEX = 1
private const val DESTINATION_HUB_ID_INDEX = 2
private const val DISTANCE_KM_INDEX = 3
private const val TYPICAL_DELAY_MIN_INDEX = 4

fun parseRoutes(lines: List<String>): List<RouteRaw> {
    if (lines.isEmpty()) {
        return emptyList()
    }

    val expectedColumnCount = getExpectedColumnCount(lines.first())
    val routes = mutableListOf<RouteRaw>()

    for (line in skipHeader(lines)) {
        if (line.isBlank()) {
            continue
        }

        val columns = extractCleanColumns(line)

        if (!isValidRouteRaw(columns, expectedColumnCount, line)) {
            continue
        }

        routes.add(
            RouteRaw(
                id = columns[ROUTE_ID_INDEX],
                originHubId = columns[ORIGIN_HUB_ID_INDEX],
                destinationHubId = columns[DESTINATION_HUB_ID_INDEX],
                distanceKm = parsePositiveDoubleOrInvalid(columns[DISTANCE_KM_INDEX]),
                typicalDelayMin = parseNonNegativeIntOrInvalid(columns[TYPICAL_DELAY_MIN_INDEX])
            )
        )
    }

    return routes
}

private fun isValidRouteRaw(
    columns: List<String>,
    expectedColumnCount: Int,
    originalLine: String
): Boolean {
    if (!hasExpectedColumnCount(columns, expectedColumnCount)) {
        println("Warning: Invalid column count -> $originalLine")
        return false
    }

    if (!hasRequiredRouteFields(columns)) {
        println("Warning: Missing route ID, origin, or destination -> $originalLine")
        return false
    }

    if (!isValidRouteDistance(columns[DISTANCE_KM_INDEX])) {
        println("Warning: Invalid distance value -> $originalLine")
        return false
    }

    return true
}

private fun hasRequiredRouteFields(columns: List<String>): Boolean {
    return isNotBlank(columns[ROUTE_ID_INDEX]) &&
            isNotBlank(columns[ORIGIN_HUB_ID_INDEX]) &&
            isNotBlank(columns[DESTINATION_HUB_ID_INDEX])
}

private fun isValidRouteDistance(distance: String): Boolean {
    return parsePositiveDoubleOrInvalid(distance) != INVALID_DOUBLE_VALUE
}