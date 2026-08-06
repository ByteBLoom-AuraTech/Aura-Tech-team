package domain.model

import algorithm.CargoQueueQuickSort

data class Warehouse(
    val id: String,
    val name: String,
    val regionalZone: String
) {
    private val _cargoQueue = mutableListOf<Package>()
    private val _outgoingRoutes = mutableListOf<Route>()
    private val _stationedVehicles = mutableListOf<Vehicle>()

    val cargoQueue: List<Package> get() = _cargoQueue
    val outgoingRoutes: List<Route> get() = _outgoingRoutes
    val stationedVehicles: List<Vehicle> get() = _stationedVehicles

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

    fun addVehicle(vehicle: Vehicle) {
        if (!_stationedVehicles.contains(vehicle)) {
            _stationedVehicles.add(vehicle)
        }
    }
    private val cargoSorter = CargoQueueQuickSort()

    fun sortCargoQueue() {
        cargoSorter.sortPackagesByWeightDescending(_cargoQueue)
    }
}