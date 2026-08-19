package domain.logic.pricing.additionalpricing

interface PackageComponent {

    fun calculateTransitRate(baseTransitRate: Double): Double
}