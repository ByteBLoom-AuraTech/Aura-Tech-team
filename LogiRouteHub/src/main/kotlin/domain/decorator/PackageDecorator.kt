package LogiRouteHub.domain.decorator

abstract class PackageDecorator(val wrap: PackageComponent) : PackageComponent {
    override fun calculateTransitRate(AuraFees: Double): Double {
        return (wrap.calculateTransitRate() * AuraFees) + wrap.calculateTransitRate()
    }

    override fun getDescription(): String {
        return "Package Decorator"
    }
}