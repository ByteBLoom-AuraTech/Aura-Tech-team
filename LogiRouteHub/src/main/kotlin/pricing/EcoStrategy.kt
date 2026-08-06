package domain.pricing


class EcoStrategy : DispatchStrategy{

    override fun calculateTransitCost(distance: Double , weight: Double): Double{
        val EcoCost = distance * 3.5 + weight * 1.5
        return EcoCost * 0.80 // discount 20%
    }

    override fun getPriorityMultiplier(): Double{
        return 0.8 // discount 20%
    }

}