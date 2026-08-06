package data.processing

import data.dataholders.FleetRaw
import data.dataholders.PackageRaw
import data.dataholders.RouteRaw
import data.dataholders.WarehouseRaw
import domain.model.Priority


fun parseFleet(lines: List<String>): List<FleetRaw> {
    val fleetList = mutableListOf<FleetRaw>()
    val dataLines = lines.drop(1)

    for (line in dataLines) {
        val columns = line.split(",")
        if (columns.size < 4) continue

        val vehicleId = columns[0].trim().uppercase()
        val currentHubId = columns[1].trim().uppercase()

        val capacityString = columns[2].trim().lowercase().replace("kg", "").trim()
        val maxCapacityKg = capacityString.toDoubleOrNull() ?: continue

        val costString = columns[3].trim()
        val costPerKm = costString.toDoubleOrNull() ?: continue

        val rawFleet = FleetRaw(vehicleId, currentHubId, maxCapacityKg, costPerKm)
        fleetList.add(rawFleet)
    }

    return fleetList
}

fun parsePackages(lines: List<String>): List<PackageRaw> {
    val packageList = mutableListOf<PackageRaw>()
    val dataLines = lines.drop(1)

    for (line in dataLines) {
        val columns = line.split(",")
        if (columns.size < 5) continue

        val id = columns[0].trim().uppercase()

        val weightString = columns[1].trim().lowercase().replace("kg", "").trim()
        if (weightString.lowercase() == "null" || weightString.isEmpty()) continue
        val weight = weightString.toDoubleOrNull() ?: continue

        val originHubId = columns[2].trim().uppercase()
        val destinationHubId = columns[3].trim().uppercase()
        val priority = parsePriorityOrDefault(columns[4])
        val rawPackage = PackageRaw(id, weight, destinationHubId, originHubId, priority)
        packageList.add(rawPackage)
    }

    return packageList
}

fun parseRoutes(lines: List<String>): List<RouteRaw> {
    val routeList = mutableListOf<RouteRaw>()
    val dataLines = lines.drop(1)

    for (line in dataLines) {
        val columns = line.split(",")
        if (columns.size < 5) continue

        val routeId = columns[0].trim().uppercase()
        val originHubId = columns[1].trim().uppercase()
        val destinationHubId = columns[2].trim().uppercase()

        val distanceKm = columns[3].trim().toDoubleOrNull() ?: continue
        val typicalDelayMin = columns[4].trim().toIntOrNull() ?: continue

        val rawRoute = RouteRaw(routeId, originHubId, destinationHubId, distanceKm, typicalDelayMin)
        routeList.add(rawRoute)
    }

    return routeList
}

fun parseWarehouses(lines: List<String>): List<WarehouseRaw> {
    val warehouseList = mutableListOf<WarehouseRaw>()
    val dataLines = lines.drop(1)

    for (line in dataLines) {
        val columns = line.split(",")
        if (columns.size < 5) continue

        val id = columns[0].trim().uppercase()
        val name = columns[1].trim()
        val regionalZone = columns[2].trim().uppercase()
        val latitude = if (columns[3].trim() == "N/A") 0.0 else columns[3].trim().toDoubleOrNull() ?: 0.0
        val longitude = if (columns[4].trim() == "N/A") 0.0 else columns[4].trim().toDoubleOrNull() ?: 0.0

        val rawWarehouse = WarehouseRaw(id, name, regionalZone, latitude, longitude)
        warehouseList.add(rawWarehouse)
    }

    return warehouseList
}

private fun parsePriorityOrDefault(rawPriority: String): Priority {
    val formattedPriority = rawPriority.trim().uppercase()

    for (priority in Priority.values()) {
        if (priority.name == formattedPriority) {
            return priority
        }
    }

    return Priority.LOW
}