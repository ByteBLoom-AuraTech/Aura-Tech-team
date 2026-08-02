package domain.pricing

class FragileStrategy : ExpressStrategy{

    override fun calculateTransitCost(distance: Double , weight: Double): Double{
        return super.calculateTransitCost(distance,weight) + 15.0
    }

    override fun getPriorityMultiplier(): Double{
        return 1.0
    }

}