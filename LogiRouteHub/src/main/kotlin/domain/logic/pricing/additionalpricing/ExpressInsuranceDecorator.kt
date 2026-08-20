package domain.logic.pricing.additionalpricing

import domain.logic.pricing.config.AdditionalPricingConfig

class ExpressInsuranceDecorator(
    packageComponent: PackageComponent,
    private val additionalPricingConfig: AdditionalPricingConfig
) : PackageDecorator(packageComponent) {

    override fun calculateTransitRate(baseTransitRate: Double, auraFees: Double): Double {
        val baseRate = packageComponent.calculateTransitRate(baseTransitRate, auraFees)
        val addedFee = additionalPricingConfig.expressInsuranceFee

        return baseRate + addedFee + (addedFee * auraFees)
    }
}