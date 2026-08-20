package domain.model

import domain.logic.pricing.additionalpricing.PackageComponent

data class Package(
    val id: String,
    val weight: Double,
    val priority: domain.model.Priority,
    val originHub: Warehouse,
    val destinationHub: Warehouse
) : PackageComponent {

    override fun calculateTransitRate(baseTransitRate: Double, AuraFees: Double): Double {
        return baseTransitRate * AuraFees
    }
}