package LogiRouteHub.domain.decorator

abstract class PackageDecorator (val wrap: PackageComponent): PackageComponent {

    override fun calculateTransitRate(): Double{
        return wrap.calculateTransitRate()
    }
    override fun getDescription(): String{
        return "Package Decorator"
    }
}