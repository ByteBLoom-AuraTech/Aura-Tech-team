package builder

import domain.model.Package
import domain.model.Route
import domain.model.Vehicle
import domain.model.Warehouse

import data.dataholders.FleetRaw
import data.dataholders.PackageRaw
import data.dataholders.RouteRaw
import data.dataholders.WarehouseRaw

class DomainGraphBuilder {

    private fun buildWarehouseIndex(
        warehouses: List<WarehouseRaw>
    ): Map<String, Warehouse> {
        val warehouseList = warehouses.map {
            Warehouse(
                id = it.id,
                name = it.name,
                regionalZone = it.regionalZone
            )
        }
        return warehouseList.associateBy { it.id }
    }

    private fun filterValidPackages(
        packages: List<PackageRaw>,
        warehouseIndex: Map<String, Warehouse>
    ): List<PackageRaw> {
        return packages.filter {
            warehouseIndex.containsKey(it.originHubId) && warehouseIndex.containsKey(it.destinationHubId)
        }
    }

    private fun filterValidVehicles(
        vehicles: List<FleetRaw>,
        warehouseIndex: Map<String, Warehouse>
    ): List<FleetRaw> {
        return vehicles.filter { warehouseIndex.containsKey(it.currentHubId) }
    }

    private fun filterValidRoutes(
        routes: List<RouteRaw>,
        warehouseIndex: Map<String, Warehouse>
    ): List<RouteRaw> {
        return routes.filter {
            warehouseIndex.containsKey(it.originHubId) && warehouseIndex.containsKey(it.destinationHubId)
        }
    }

    private fun buildVehicleList(
        vehicles: List<FleetRaw>,
        warehouseIndex: Map<String, Warehouse>
    ): List<Vehicle> {
        return vehicles.map {
            Vehicle(
                id = it.vehicleId,
                maxCapacityKg = it.maxCapacityKg,
                costPerKm = it.costPerKm,
                currentHub = warehouseIndex.getValue(it.currentHubId)
            )
        }
    }

    private fun buildPackageList(
        packages: List<PackageRaw>,
        warehouseIndex: Map<String, Warehouse>
    ): List<Package> {
        return packages.map {
            Package(
                id = it.id,
                weight = it.weight,
                priority = it.priority,
                originHub = warehouseIndex.getValue(it.originHubId),
                destinationHub = warehouseIndex.getValue(it.destinationHubId)
            )
        }
    }

    private fun buildRouteList(
        routes: List<RouteRaw>,
        warehouseIndex: Map<String, Warehouse>
    ): List<Route> {
        return routes.map {
            Route(
                id = it.routeId,
                distanceKm = it.distanceKm,
                typicalDelayMin = it.typicalDelayMin,
                origin = warehouseIndex.getValue(it.originHubId),
                destination = warehouseIndex.getValue(it.destinationHubId)
            )
        }
    }

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

    fun buildGraph(
        warehouses: List<WarehouseRaw>,
        packages: List<PackageRaw>,
        vehicles: List<FleetRaw>,
        routes: List<RouteRaw>
    ): List<Warehouse> {
        val warehouseIndex = buildWarehouseIndex(warehouses)
        val warehouseList = warehouseIndex.values.toList()

        val validPackages = filterValidPackages(packages, warehouseIndex)
        val validVehicles = filterValidVehicles(vehicles, warehouseIndex)
        val validRoutes = filterValidRoutes(routes, warehouseIndex)
        val vehicleList = buildVehicleList(validVehicles, warehouseIndex)
        val packageList = buildPackageList(validPackages, warehouseIndex)
        val routeList = buildRouteList(validRoutes, warehouseIndex)

        connectGraph(
            warehouseList,
            vehicleList,
            packageList,
            routeList
        )
        return warehouseList
    }
}
