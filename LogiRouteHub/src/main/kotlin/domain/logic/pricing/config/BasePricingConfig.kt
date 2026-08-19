package domain.logic.pricing.config

data class BasePricingConfig(
    val ecoWeightRate: Double = 0.8,
    val ecoDistanceRate: Double = 0.5,

    val expressWeightRate: Double = 3.0,
    val expressDistanceRate: Double = 2.3,

    val fragileWeightRate: Double = 1.0,
    val fragileDistanceRate: Double = 1.3,
    val fragileSafetyFee: Double = 8.0,

    val ecoUrgentMultiplier: Double = 1.1,
    val ecoStandardMultiplier: Double = 1.0,
    val ecoLowMultiplier: Double = 0.8,

    val expressUrgentMultiplier: Double = 1.4,
    val expressStandardMultiplier: Double = 1.1,
    val expressLowMultiplier: Double = 1.0,

    val fragileUrgentMultiplier: Double = 1.5,
    val fragileStandardMultiplier: Double = 1.0,
    val fragileLowMultiplier: Double = 0.8
)

