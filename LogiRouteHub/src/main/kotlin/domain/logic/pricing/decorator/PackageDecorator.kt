package domain.logic.pricing.decorator

abstract class PackageDecorator(
    protected val packageComponent: PackageComponent
) : PackageComponent {

    override fun calculateTransitRate(baseTransitRate: Double): Double {
        return packageComponent.calculateTransitRate(baseTransitRate)
    }
}