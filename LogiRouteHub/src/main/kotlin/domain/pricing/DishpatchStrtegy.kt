package domain.pricing
import domain.model.Priority

interface DispatchStrategy {

    fun calculateTransitCost(weight: Double , distanceKm: Double):Double

    fun getPriorityMultiplier(priority: Priority): Double
}