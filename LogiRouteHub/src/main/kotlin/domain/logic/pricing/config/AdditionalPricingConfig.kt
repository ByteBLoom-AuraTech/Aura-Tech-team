package domain.logic.pricing.config

data class AdditionalPricingConfig(
    val fragileHandlingFee: Double = 8.0,
    val coldChainMultiplier: Double = 1.25,
    val expressInsuranceFee: Double = 12.0,
    val auraFees: Double = 0.01
)