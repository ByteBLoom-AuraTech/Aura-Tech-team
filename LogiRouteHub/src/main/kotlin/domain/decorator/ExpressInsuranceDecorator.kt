package LogiRouteHub.domain.decorator

class ExpressInsuranceDecorator(wrap: PackageComponent , private val expressInsurance: Double): PackageDecorator(wrap){

    override fun calculateTransitRate(): Double {
        return wrap.calculateTransitRate() + expressInsurance
    }
    override fun getDescription(): String {
        return wrap.getDescription() + "Express Insurance"
    }
}