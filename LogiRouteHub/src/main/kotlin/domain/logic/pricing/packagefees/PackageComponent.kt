package domain.logic.pricing.packagefees

interface PackageComponent {

    fun calculateTransitRate(baseTransitRate: Double): Double
}