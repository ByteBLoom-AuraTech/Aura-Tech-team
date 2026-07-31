package domain.model

class Vehicle(
    val vehicleId: String,
    val maxCapacityKg: Double,
    val costPerKm: Double,
    val currentHub: Warehouse
)