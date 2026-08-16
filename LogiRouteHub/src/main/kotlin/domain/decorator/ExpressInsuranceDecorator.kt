package LogiRouteHub.domain.decorator

class ExpressInsuranceDecorator(wrap: PackageComponent , private val expressInsurance: Double): PackageDecorator(wrap){
    override fun calculateTransitRate(AuraFees: Double): Double {
        val baseRate = wrap.calculateTransitRate(AuraFees) + expressInsurance
        return baseRate + (baseRate * AuraFees)
    }
    override fun getDescription(): String {
        return wrap.getDescription() + " Express Insurance"
    }
}