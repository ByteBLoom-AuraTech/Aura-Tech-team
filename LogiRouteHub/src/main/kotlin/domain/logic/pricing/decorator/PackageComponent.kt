package domain.logic.pricing.decorator

interface PackageComponent {

    fun calculateTransitRate(baseTransitRate: Double): Double
}