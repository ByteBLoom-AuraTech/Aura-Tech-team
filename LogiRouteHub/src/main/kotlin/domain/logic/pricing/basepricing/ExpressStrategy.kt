package domain.logic.pricing.basepricing
import domain.logic.pricing.config.BasePricingConfig
import domain.model.Priority

class ExpressStrategy( private val pricingConfig: BasePricingConfig) : DispatchStrategy {

    override fun calculateTransitCost(
        weight: Double,
        distanceKm: Double
    ): Double {
        return (weight *pricingConfig.expressWeightRate) +
                (distanceKm * pricingConfig.expressDistanceRate)
    }

    override fun getPriorityMultiplier(priority: Priority): Double {
        return when (priority) {
            Priority.URGENT -> pricingConfig.expressUrgentMultiplier
            Priority.STANDARD ->pricingConfig.expressStandardMultiplier
            Priority.LOW ->pricingConfig.expressLowMultiplier
        }
    }

}