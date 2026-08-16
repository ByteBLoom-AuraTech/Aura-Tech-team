package LogiRouteHub.domain.decorator

class ColdChainDecorator(wrap: PackageComponent , private val coldChain: Double): PackageDecorator(wrap) {
    override fun calculateTransitRate(AuraFees: Double): Double {
        val baseRate = wrap.calculateTransitRate(AuraFees) * coldChain
        return baseRate + (baseRate * AuraFees)
    }
    override fun getDescription(): String {
        return wrap.getDescription() + "Cold Chain"
    }
}