package domain.model
import domain.logic.pricing.packagefees.PackageComponent

data class Package(
    val id: String,
    val weight: Double,
    val priority: domain.model.Priority,
    val originHub: Warehouse,
    val destinationHub: Warehouse
) : PackageComponent {

    override fun calculateTransitRate(baseTransitRate: Double): Double {
        return baseTransitRate
    }
}