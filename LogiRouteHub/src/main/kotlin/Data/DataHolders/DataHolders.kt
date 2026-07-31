package LogiRouteHub

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
    val typicalDelayMin: Int
)

data class WarehouseRaw(
    val id: String,
    val name: String,
    val regionalZone: String
)

fun readFile(filePath: String){
    val file = File(filePath)
    if (!file.exists()) return
    val fileContent = file.readText()
    val startIndex = skipHeader(fileContent)
    processDataLines(fileContent, startIndex)
}