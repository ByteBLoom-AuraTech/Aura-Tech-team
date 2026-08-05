package domain.pricing

import LogiRouteHub.Priority

class FragileStrategy : DispatchStrategy {

    override fun calculateTransitCost(distance: Double, weight: Double): Double {
        return (distance * 3.5 + weight * 1.5) + 15.0
    }

    override fun getPriorityMultiplier(): Double {
        return 1.0
    }

}