package domain.logic.pricing.config

data class PackageFeesPricingConfig(
    val fragileHandlingFee: Double = 8.0,
    val coldChainMultiplier: Double = 1.25,
    val expressInsuranceFee: Double = 12.0
)