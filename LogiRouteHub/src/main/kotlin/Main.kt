package LogiRouteHub

import algorithm.PackageAssignmentRing
import algorithm.PackageMapping
import algorithm.RingVehicle
import algorithm.distributeAllPackages
import algorithm.runOutageForBreakdown

import builder.DomainGraphBuilder
import domain.model.Warehouse

import data.dataholders.RouteRaw
import data.dataholders.PackageRaw
import data.dataholders.WarehouseRaw
import data.dataholders.FleetRaw

import data.processing.loadFleets
import data.processing.loadPackages
import data.processing.loadRoutes
import data.processing.loadWarehouses

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

    // Task 2 Execution Point
    testPackageAssignmentRing()
}

private fun loadRawData(): RawData {
    return RawData(
        warehouses = loadWarehouses(),
        packages = loadPackages(),
        vehicles = loadFleets(),
        routes = loadRoutes()
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

// ==================================================
// TASK 2: CONSISTENT HASHING - PACKAGE ASSIGNMENT RING
// ==================================================

private fun testPackageAssignmentRing() {
    println("\n==================================================")
    println("  CONSISTENT HASHING - PACKAGE ASSIGNMENT RING    ")
    println("==================================================\n")

    val initialVehicles = setupInitialVehicles()
    val samplePackageIds = generateSamplePackageIds(count = 30)

    val initialDistribution = distributeAllPackages(
        packageIds = samplePackageIds,
        vehicles = initialVehicles
    )

    println("--- INITIAL PACKAGE ASSIGNMENTS ---")
    initialDistribution.values.forEach { mapping ->
        println(mapping)
    }

    runOutageSimulation(initialVehicles, initialDistribution)
}

private fun setupInitialVehicles(): List<RingVehicle> {
    return listOf(
        RingVehicle(id = "Vehicle-15", slot = PackageAssignmentRing.VEHICLE_SLOT_A),
        RingVehicle(id = "Vehicle-40", slot = PackageAssignmentRing.VEHICLE_SLOT_B),
        RingVehicle(id = "Vehicle-65", slot = PackageAssignmentRing.VEHICLE_SLOT_C),
        RingVehicle(id = "Vehicle-90", slot = PackageAssignmentRing.VEHICLE_SLOT_D)
    )
}

private fun runOutageSimulation(
    initialVehicles: List<RingVehicle>,
    initialDistribution: Map<String, PackageMapping>
) {
    val brokenVehicleId = "Vehicle-40"
    val expectedFallbackVehicleSlot = PackageAssignmentRing.VEHICLE_SLOT_C
    val remainingVehicles = initialVehicles.filterNot { it.id == brokenVehicleId }

    println("\n==================================================")
    println(">>> SIMULATING BREAKDOWN OF VEHICLE: $brokenVehicleId <<<")
    println("==================================================\n")

    val updatedDistribution = runOutageForBreakdown(
        previousDistribution = initialDistribution,
        brokenVehicleId = brokenVehicleId,
        remainingVehicles = remainingVehicles
    )

    verifyAndOutputResults(
        initialDistribution = initialDistribution,
        updatedDistribution = updatedDistribution,
        brokenVehicleId = brokenVehicleId,
        expectedFallbackSlot = expectedFallbackVehicleSlot
    )
}

private fun verifyAndOutputResults(
    initialDistribution: Map<String, PackageMapping>,
    updatedDistribution: Map<String, PackageMapping>,
    brokenVehicleId: String,
    expectedFallbackSlot: Int
) {
    var reroutedCargoCount = 0
    var unaffectedCargoCount = 0

    println("--- CARGO RE-ROUTING AUDIT & ASSERTIONS ---")

    initialDistribution.forEach { (packageId, oldMapping) ->
        val newMapping = updatedDistribution.getValue(packageId)

        if (oldMapping.vehicleId == brokenVehicleId) {
            check(newMapping.vehicleSlot == expectedFallbackSlot) {
                "Assertion Failure: Package $packageId failed to shift"
            }
            println("[RE-ROUTED] Package [$packageId] -> Shifted to [${newMapping.vehicleId}]")
            reroutedCargoCount++
        } else {
            check(oldMapping.vehicleId == newMapping.vehicleId && oldMapping.vehicleSlot == newMapping.vehicleSlot) {
                "Assertion Failure: Package $packageId illegally migrated"
            }
            println("[UNCHANGED] Package [$packageId] -> Preserved on [${newMapping.vehicleId}]")
            unaffectedCargoCount++
        }
    }

    println("\n==================================================")
    println("VERIFICATION COMPLETE & ASSERTIONS PASSED:")
    println("✓ Re-routed Packages: $reroutedCargoCount")
    println("✓ Unaffected Packages: $unaffectedCargoCount")
    println("==================================================")
}

private fun generateSamplePackageIds(count: Int): List<String> {
    return (1..count).map { index -> "PKG-TEST-$index" }
}
