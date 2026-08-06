package domain.pricing


interface DispatchStrategy {

    fun calculateTransitCost(distance: Double , weight: Double):Double

    fun getPriorityMultiplier(): Double
}