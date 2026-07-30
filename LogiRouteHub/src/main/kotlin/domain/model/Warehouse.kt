package domain.model

class Warehouse(
    val id: String,
    val name: String,
    val regionalZone: String
) {
    private val _cargoQueue = mutableListOf<Package>()
    private val _outgoingRoutes = mutableListOf<Route>()

    val cargoQueue: List<Package> get() = _cargoQueue
    val outgoingRoutes: List<Route> get() = _outgoingRoutes

    fun addPackage(pkg: Package) {
        if (!_cargoQueue.contains(pkg)) {
            _cargoQueue.add(pkg)
        }
    }

    fun addRoute(route: Route) {
        if (!_outgoingRoutes.contains(route)) {
            _outgoingRoutes.add(route)
        }
    }
}