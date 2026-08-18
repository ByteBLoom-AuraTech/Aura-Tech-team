package domain.logic.pricing.packagefees

import domain.logic.pricing.config.PackageFeesPricingConfig

class ColdChainDecorator(
    packageComponent: PackageComponent,
    private val pricingConfig: PackageFeesPricingConfig
) : PackageDecorator(packageComponent) {

    override fun calculateTransitRate(baseTransitRate: Double): Double {
        val baseTransitRate =
            packageComponent.calculateTransitRate(baseTransitRate)

        return baseTransitRate * pricingConfig.coldChainMultiplier
    }
}