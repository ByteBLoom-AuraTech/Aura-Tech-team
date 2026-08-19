package domain.logic.pricing.additionalpricing

import domain.logic.pricing.config.AdditionalPricingConfig

class ExpressInsuranceDecorator(
    packageComponent: PackageComponent,
    private val additionalPricingConfig: AdditionalPricingConfig
) : PackageDecorator(packageComponent) {

    override fun calculateTransitRate(baseTransitRate: Double, AuraFees: Double): Double {
        val baseRate = packageComponent.calculateTransitRate(baseTransitRate, AuraFees)
        val addedFee = additionalPricingConfig.expressInsuranceFee

        return baseRate + addedFee + (addedFee * AuraFees)
    }
}