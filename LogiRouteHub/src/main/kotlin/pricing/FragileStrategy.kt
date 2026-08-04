package domain.pricing

import LogiRouteHub.Priority

class FragileStrategy : DispatchStrategy() {

    override fun calculateTransitCost(distance: Double , weight: Double): Double{
        return super.calculateTransitCost(distance,weight) + 15.0
    }

    override fun getPriorityMultiplier( priority: Priority): Double{
        return 1.0
    }

}