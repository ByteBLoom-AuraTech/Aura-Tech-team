package domain.pricing

import LogiRouteHub.Priority

open class ExpressStrategy : DispatchStrategy{

    override fun calculateTransitCost(distance: Double , weight: Double): Double{
        val ExpressCost = distance * 3.5 + weight * 1.5 // i have chosen the numbers dependes on google researches on real world
        return ExpressCost
    }

    override fun getPriorityMultiplier( priority: Priority): Double{
        return 2.0
    }

}