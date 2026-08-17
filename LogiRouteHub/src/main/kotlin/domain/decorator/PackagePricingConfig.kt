package LogiRouteHub.domain.decorator


data class PackagePricingConfig(
    val fragileHandlingFee: Double = 8.0,
    val coldChainMultiplier: Double = 1.25,
    val expressInsuranceFee: Double = 12.0
)
