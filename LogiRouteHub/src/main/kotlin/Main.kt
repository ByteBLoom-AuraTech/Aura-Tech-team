package LogiRouteHub

import builder.DomainGraphBuilder
import domain.model.Warehouse

import data.dataholders.PackageRow
import data.dataholders.FleetRow
import data.dataholders.RouteRow
import data.dataholders.WarehouseRaw

private data class RawData(
    val warehouses: List<WarehouseRaw>,
    val packages: List<PackageRaw>,
    val vehicles: List<FleetRaw>,
    val routes: List<RouteRaw>
)

fun main() {
    println("LogiRouteHub System")


    val rawData = loadRawData()

    if (rawData.warehouses.isEmpty()) {
        println("ERROR: No warehouses found. Cannot build the domain graph.")
        return
    }

    val connectedWarehouses = buildDomainGraph(rawData)


    testPricing(connectedWarehouses)
    testSorting(connectedWarehouses)
    verifyGraph(connectedWarehouses)
}


private fun loadRawData(): RawData {
    val warehouseRaw = parseWarehouse("src/main/resources/warehouse.csv")
    val packageRaw = parsePackages("src/main/resources/packages.csv")
    val vehicleRaw = parseFleet("src/main/resources/fleet.csv")
    val routeRaw = parseRoutes("src/main/resources/routes.csv")

    return RawData(
        warehouses = warehouseRaw,
        packages = packageRaw,
        vehicles = vehicleRaw,
        routes = routeRaw
    )
}


private fun buildDomainGraph(rawData: RawData): List<Warehouse> {
    println("Building Domain Graph")

    val builder = DomainGraphBuilder()

    val connectedWarehouses = builder.buildGraph(
        warehouses = rawData.warehouses,
        packages = rawData.packages,
        vehicles = rawData.vehicles,
        routes = rawData.routes
    )

    println("Connected hubs: ${connectedWarehouses.size}")
    return connectedWarehouses
}

private fun testPricing(connectedWarehouses: List<Warehouse>) {
    println("Testing Route Pricing")

    val sampleHub = connectedWarehouses.firstOrNull()
    val samplePackage = sampleHub?.cargoQueue?.firstOrNull()
    val sampleRoute = sampleHub?.outgoingRoutes?.firstOrNull()

    if (samplePackage != null && sampleRoute != null) {
        println("Testing package ID: ${samplePackage.id} (Weight: ${samplePackage.weight}kg)")
        println("Route Distance: ${sampleRoute.distanceKm} km")


        val basePrice = sampleRoute.distanceKm * 1.5
        println("Estimated Base Shipping Price: \$$basePrice")
    } else {
        println("No package or route available to test pricing.")
    }
}


private fun testSorting(connectedWarehouses: List<Warehouse>) {
    println("Testing QuickSort Cargo Queue")

    val firstHub = connectedWarehouses.firstOrNull()

    if (firstHub != null && firstHub.cargoQueue.isNotEmpty()) {
        println("Before Sorting (${firstHub.id}):")
        firstHub.cargoQueue.forEachIndexed { index, pkg ->
            println("  $index: ${pkg.id} (${pkg.weight}kg)")
        }


        firstHub.sortCargoQueue()

        println("After Sorting (${firstHub.id})")
        firstHub.cargoQueue.forEachIndexed { index, pkg ->
            println("  $index: ${pkg.id} (${pkg.weight}kg)")
        }
    } else {
        println("First hub has no packages to sort.")
    }
}


private fun verifyGraph(connectedWarehouses: List<Warehouse>) {
    println("Quick Verification")

    val firstHub = connectedWarehouses.firstOrNull()
    if (firstHub != null) {
        println("First hub ID: ${firstHub.id} (${firstHub.name})")
        println("  Packages count: ${firstHub.cargoQueue.size}")
        println("  Vehicles count: ${firstHub.stationedVehicles.size}")
        println("  Routes count:   ${firstHub.outgoingRoutes.size}")
    } else {
        println("No hubs built.")
    }
}