package LogiRouteHub.domain.decorator

class ColdChainDecorator(wrap: PackageComponent , private val coldChain: Double): PackageDecorator(wrap) {

    override fun calculateTransitRate(): Double {
        return wrap.calculateTransitRate() + coldChain
    }
    override fun getDescription(): String {
        return wrap.getDescription() + "Cold Chain"
    }
}