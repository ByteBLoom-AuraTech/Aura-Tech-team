package domain.logic.pricing.additionalpricing

abstract class PackageDecorator(
    protected val packageComponent: PackageComponent
) : PackageComponent {

    override fun calculateTransitRate(baseTransitRate: Double): Double {
        return packageComponent.calculateTransitRate(baseTransitRate)
    }
}