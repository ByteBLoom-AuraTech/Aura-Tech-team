package LogiRouteHub.domain.decorator

class FragileHandlingDecorator(wrap: PackageComponent , private val fragile: Double): PackageDecorator(wrap) {
    override fun calculateTransitRate(AuraFees: Double): Double {
        val baseRate = wrap.calculateTransitRate(AuraFees) + fragile
        return baseRate + (baseRate * AuraFees)
    }
    override fun getDescription(): String {
        return wrap.getDescription() + "Fragile Handling"
    }
}
