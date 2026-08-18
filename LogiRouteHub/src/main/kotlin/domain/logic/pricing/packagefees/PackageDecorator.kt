package domain.logic.pricing.packagefees

abstract class PackageDecorator(
    protected val packageComponent: PackageComponent
) : PackageComponent {

    override fun calculateTransitRate(baseTransitRate: Double): Double {
        return packageComponent.calculateTransitRate(baseTransitRate)
    }
}