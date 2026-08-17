package LogiRouteHub.domain.decorator

interface PackageComponent {
    fun calculateTransitRate(AuraFees: Double = .01): Double
    fun getDescription(): String
}
