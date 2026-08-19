package domain.logic.pricing.additionalpricing

import domain.logic.pricing.config.AdditionalPricingConfig

class FragileHandlingDecorator(
    packageComponent: PackageComponent,
    private val additionalPricingConfig: AdditionalPricingConfig
) : PackageDecorator(packageComponent) {

    override fun calculateTransitRate(baseTransitRate: Double): Double {
        val decoratedTransitRate =
            packageComponent.calculateTransitRate(baseTransitRate)

        return decoratedTransitRate + additionalPricingConfig.fragileHandlingFee
    }
}