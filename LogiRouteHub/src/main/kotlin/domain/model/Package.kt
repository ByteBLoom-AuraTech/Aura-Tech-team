package domain.model

class Package(
    val id: String,
    val weight: Double,
    val priority: Int,
    val origin: Warehouse,
    val destination: Warehouse
)