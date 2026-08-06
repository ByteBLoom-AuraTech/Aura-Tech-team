package domain.pricing

class RoutePricingEngine (var strategy : DispatchStrategy) {

    fun calculatePrice(distance: Double , weight: Double):Double{
        val transitCost = strategy.calculateTransitCost(distance, weight)

        val priorityMultiplier = strategy.getPriorityMultiplier()

        return transitCost + priorityMultiplier
    }

}