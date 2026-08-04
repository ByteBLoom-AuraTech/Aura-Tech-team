package domain.pricing

import LogiRouteHub.Priority

interface DispatchStrategy {

    fun calculateTransitCost(distance: Double , weight: Double):Double

    fun getPriorityMultiplier( priority: Priority): Double
}