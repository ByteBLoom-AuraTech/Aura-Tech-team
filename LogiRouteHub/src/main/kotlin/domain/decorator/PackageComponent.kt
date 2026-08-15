package LogiRouteHub.domain.decorator

interface PackageComponent {

    fun calculateTransitRate(): Double
    fun getDescription(): String
}