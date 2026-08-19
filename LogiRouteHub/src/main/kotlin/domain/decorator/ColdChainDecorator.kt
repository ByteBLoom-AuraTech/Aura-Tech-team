package LogiRouteHub.domain.decorator

class ColdChainDecorator(wrap: PackageComponent , private val coldChainMultiplier: Double = 1.25): PackageDecorator(wrap) {
    override fun calculateTransitRate(AuraFees: Double): Double {
        val baseRate = wrap.calculateTransitRate(AuraFees) * coldChainMultiplier
        return baseRate + (baseRate * AuraFees)
    }
    override fun getDescription(): String {
        return wrap.getDescription() + " Cold Chain"
    }
}
