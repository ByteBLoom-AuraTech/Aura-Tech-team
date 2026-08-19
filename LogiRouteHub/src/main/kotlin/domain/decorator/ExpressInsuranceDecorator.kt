package LogiRouteHub.domain.decorator

class ExpressInsuranceDecorator(wrap: PackageComponent , private val expressInsuranceFee: Double = 12.0): PackageDecorator(wrap){
    override fun calculateTransitRate(AuraFees: Double): Double {
        val baseRate = wrap.calculateTransitRate(AuraFees) * expressInsuranceFee
        return baseRate + (baseRate * AuraFees)
    }
    override fun getDescription(): String {
        return wrap.getDescription() + " Express Insurance"
    }
}
