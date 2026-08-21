package domain.builder

import domain.model.Package
import domain.model.Route
import domain.model.Vehicle
import domain.model.Warehouse

class DomainGraphBuilder(private val domainGraphInput: DomainGraphInput) {

    private fun connectGraph(
        warehouseList: List<Warehouse>,
        vehicleList: List<Vehicle>,
        packageList: List<Package>,
        routeList: List<Route>
    ) {
        val vehiclesByWarehouse = vehicleList.groupBy { it.currentHub.id }
        val packagesByWarehouse = packageList.groupBy { it.originHub.id }
        val routesByWarehouse = routeList.groupBy { it.origin.id }

        for (warehouse in warehouseList) {
            vehiclesByWarehouse[warehouse.id]?.forEach { warehouse.addVehicle(it) }
            packagesByWarehouse[warehouse.id]?.forEach { warehouse.addPackage(it) }
            routesByWarehouse[warehouse.id]?.forEach { warehouse.addRoute(it) }
        }
    }

    private fun loadDomainGraphData(): DomainGraphData {
        return DomainGraphData(
            warehouses = domainGraphInput.warehouseRepository.getAll(),
            packages = domainGraphInput.packageRepository.getAll(),
            vehicles = domainGraphInput.vehicleRepository.getAll(),
            routes = domainGraphInput.routeRepository.getAll()
        )
    }

    fun buildGraph(): List<Warehouse> {
        val domainGraphData = loadDomainGraphData()

        connectGraph(
            warehouseList = domainGraphData.warehouses,
            vehicleList = domainGraphData.vehicles,
            packageList = domainGraphData.packages,
            routeList = domainGraphData.routes
        )
        return domainGraphData.warehouses
    }
}
