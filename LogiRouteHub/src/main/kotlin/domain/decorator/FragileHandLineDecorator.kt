package LogiRouteHub.domain.decorator

class FragileHandLineDecorator(wrap: PackageComponent , private val fragile: Double): PackageDecorator(wrap) {

    override fun calculateTransitRate(): Double {
        return wrap.calculateTransitRate() + fragile
    }
    override fun getDescription(): String {
        return wrap.getDescription() + "Fragile Handling"
    }
}