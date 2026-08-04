package domain.model

import data.dataholders.Priority

data class Package(
    val id: String,
    val weight: Double,
    val priority: Priority,
    val originHub: Warehouse,
    val destinationHub: Warehouse
)