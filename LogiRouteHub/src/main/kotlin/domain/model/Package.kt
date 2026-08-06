package domain.model

data class Package(
    val id: String,
    val weight: Double,
    val priority: domain.model.Priority,
    val originHub: Warehouse,
    val destinationHub: Warehouse
)