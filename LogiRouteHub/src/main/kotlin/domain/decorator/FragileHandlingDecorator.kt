package LogiRouteHub.domain.decorator

class FragileHandlingDecorator(wrap: PackageComponent , private val fragileHandlingFee: Double = 8.0): PackageDecorator(wrap) {
    override fun calculateTransitRate(AuraFees: Double): Double {
        val baseRate = wrap.calculateTransitRate(AuraFees) * fragileHandlingFee
        return baseRate + (baseRate * AuraFees)
    }
    override fun getDescription(): String {
        return wrap.getDescription() + " Fragile Handling"
    }
}
