package domain.logic.pricing.additionalpricing

import domain.logic.pricing.config.AdditionalPricingConfig

class ColdChainDecorator(
    packageComponent: PackageComponent,
    private val additionalPricingConfig: AdditionalPricingConfig
) : PackageDecorator(packageComponent) {

    override fun calculateTransitRate(baseTransitRate: Double, AuraFees: Double): Double {
        val baseRate = packageComponent.calculateTransitRate(baseTransitRate, AuraFees)

        return baseRate * additionalPricingConfig.coldChainMultiplier
    }
}