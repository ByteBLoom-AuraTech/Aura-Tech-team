package domain.logic.pricing.basepricing

import domain.logic.pricing.config.BasePricingConfig
import domain.logic.pricing.basepricing.DispatchStrategy
import domain.model.Priority


class EcoStrategy(private val pricingConfig: BasePricingConfig) : DispatchStrategy {

    override fun calculateTransitCost(
        weight: Double,
        distanceKm: Double
    ): Double {
        return (weight * pricingConfig.ecoWeightRate) +
                (distanceKm * pricingConfig.ecoDistanceRate)
    }

    override fun getPriorityMultiplier(priority: Priority): Double {
        return when (priority) {
            Priority.URGENT -> pricingConfig.ecoUrgentMultiplier
            Priority.STANDARD -> pricingConfig.ecoStandardMultiplier
            Priority.LOW -> pricingConfig.ecoLowMultiplier
        }

    }
}
