package domain.logic.pricing.decorator

class ExpressInsuranceDecorator(
    packageComponent: PackageComponent,
    private val pricingConfig: PackagePricingConfig
) : PackageDecorator(packageComponent) {

    override fun calculateTransitRate(baseRate: Double): Double {
        val baseTransitRate =
            packageComponent.calculateTransitRate(baseRate)

        return baseTransitRate + pricingConfig.expressInsuranceFee
    }
}