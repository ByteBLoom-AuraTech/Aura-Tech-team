package domain.pricing

import LogiRouteHub.Priority


class RoutePricingEngine (var strategy : DispatchStrategy) {

    fun setStrategy(Strategy: DispatchStrategy){
        this.strategy = Strategy
    }

    fun calculatePrice(distance: Double , weight: Double):Double{
        val transitCost = strategy.calculateTransitCost(distance, weight)

        val priorityMultiplier = strategy.getPriorityMultiplier()

        return transitCost + priorityMultiplier
    }

}