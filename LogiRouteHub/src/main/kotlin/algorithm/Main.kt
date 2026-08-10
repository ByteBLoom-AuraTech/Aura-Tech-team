package algorithm

/**
 * Main execution point for Task 2: The Broken Truck Re-shuffling Problem.
 */
fun main() {
    println("==================================================")
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
