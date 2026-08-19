package domain.model

import domain.logic.pricing.decorator.PackageComponent

data class Package(
    val id: String,
    val weight: Double,
    val priority: Priority,
    val originHub: Warehouse,
    val destinationHub: Warehouse
) : PackageComponent {

    override fun calculateTransitRate(baseTransitRate: Double): Double {
        return baseTransitRate
    }
}