package logistics.models

/**
 * Models.kt
 *
 * Responsibility: ONLY data shape definitions (data classes).
 * No parsing logic, no file I/O, no validation logic lives here.
 */

data class Package(
    val id: String,
    val weight: Double,
    val destinationHubId: String,
    val priority: String
)

data class Warehouse(
    val id: String,
    val name: String,
    val regionalZone: String
)

data class Route(
    val routeId: String,
    val originHubId: String,
    val destinationHubId: String,
    val distanceKm: Double,
    val typicalDelayMin: Double
)

data class Fleet(
    val vehicleId: String,
    val currentHubId: String,
    val maxCapacityKg: Double,
    val costPerKm: Double
)