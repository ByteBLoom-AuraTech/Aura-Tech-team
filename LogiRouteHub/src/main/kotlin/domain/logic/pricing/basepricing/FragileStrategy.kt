package domain.logic.pricing.basepricing

import domain.logic.pricing.config.BasePricingConfig
import domain.model.Priority


class FragileStrategy(private val pricingConfig: BasePricingConfig) : DispatchStrategy {

    override fun calculateTransitCost(
        weight: Double,
        distanceKm: Double
    ): Double {
        return (weight * pricingConfig.fragileWeightRate) +
                (distanceKm * pricingConfig.fragileDistanceRate) +
                pricingConfig.fragileSafetyFee
    }

    override fun getPriorityMultiplier(priority: Priority): Double {
        return when (priority) {
            Priority.URGENT -> pricingConfig.fragileUrgentMultiplier
            Priority.STANDARD -> pricingConfig.fragileStandardMultiplier
            Priority.LOW -> pricingConfig.fragileLowMultiplier
        }
    }

}