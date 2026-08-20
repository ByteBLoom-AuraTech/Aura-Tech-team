package domain.logic.pricing.additionalpricing

import domain.logic.pricing.config.AdditionalPricingConfig

class ColdChainDecorator(
    packageComponent: PackageComponent,
    private val additionalPricingConfig: AdditionalPricingConfig
) : PackageDecorator(packageComponent) {

    override fun calculateTransitRate(baseTransitRate: Double, auraFees: Double): Double {
        val baseRate = packageComponent.calculateTransitRate(baseTransitRate, auraFees)

        return baseRate * additionalPricingConfig.coldChainMultiplier
    }
}