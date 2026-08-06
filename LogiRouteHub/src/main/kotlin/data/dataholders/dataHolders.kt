package data.dataholders




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
    val originHubId: String,
    val priority: domain.model.Priority
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
    val regionalZone: String,
    val latitude: Double,
    val longitude: Double
)
