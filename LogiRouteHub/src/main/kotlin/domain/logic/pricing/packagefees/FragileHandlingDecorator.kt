package domain.logic.pricing.packagefees

import domain.logic.pricing.config.PackageFeesPricingConfig

class FragileHandlingDecorator(
    packageComponent: PackageComponent,
    private val pricingConfig: PackageFeesPricingConfig
) : PackageDecorator(packageComponent) {

    override fun calculateTransitRate(baseTransitRate: Double): Double {
        val baseTransitRate =
            packageComponent.calculateTransitRate(baseTransitRate)

        return baseTransitRate + pricingConfig.fragileHandlingFee
    }
}