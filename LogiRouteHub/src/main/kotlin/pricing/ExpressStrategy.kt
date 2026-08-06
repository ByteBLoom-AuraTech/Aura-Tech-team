package domain.pricing


open class ExpressStrategy : DispatchStrategy {

    override fun calculateTransitCost(distance: Double, weight: Double): Double {
        val ExpressCost =
            distance * 3.5 + weight * 1.5 // I have chosen the numbers dependes on Google researches on real world
        return ExpressCost
    }

    override fun getPriorityMultiplier(): Double {
        return 2.0
    }

}