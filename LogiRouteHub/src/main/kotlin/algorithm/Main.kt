package algorithm

/**
 * Main execution point for Task 2: The Broken Truck Re-shuffling Problem.
 * Built directly on top of the algorithm methods developed by the team.
 */
fun main() {
    println("==================================================")
    println("  CONSISTENT HASHING - PACKAGE ASSIGNMENT RING    ")
    println("==================================================\n")

    // 1. Initialize Active Vehicles at slots 15, 40, 65, and 90
    val initialVehicles = listOf(
        RingVehicle(id = "Vehicle-15", slot = PackageAssignmentRing.VEHICLE_SLOT_A),
        RingVehicle(id = "Vehicle-40", slot = PackageAssignmentRing.VEHICLE_SLOT_B),
        RingVehicle(id = "Vehicle-65", slot = PackageAssignmentRing.VEHICLE_SLOT_C),
        RingVehicle(id = "Vehicle-90", slot = PackageAssignmentRing.VEHICLE_SLOT_D)
    )

    // 2. Generate sample package IDs (Ensures coverage of edge cases: wrap-around, exact hits, etc.)
    val samplePackageIds = generateSamplePackageIds(count = 30)

    // 3. Perform initial distribution using team's `distributeAllPackages` function
    val initialDistribution: Map<String, PackageMapping> = distributeAllPackages(
        packageIds = samplePackageIds,
        vehicles = initialVehicles
    )

    println("--- INITIAL PACKAGE ASSIGNMENTS ---")
    initialDistribution.values.forEach { mapping ->
        println(mapping)
    }

    // 4. Simulate breakdown of Vehicle at Slot 40 ("Vehicle-40")
    val brokenVehicleId = "Vehicle-40"
    val expectedFallbackVehicleSlot = PackageAssignmentRing.VEHICLE_SLOT_C // Slot 65

    val remainingVehicles = initialVehicles.filterNot { it.id == brokenVehicleId }

    println("\n==================================================")
    println(">>> SIMULATING BREAKDOWN OF VEHICLE: $brokenVehicleId <<<")
    println("==================================================\n")

    // 5. Re-route packages using team's `runOutageForBreakdown` function
    val updatedDistribution: Map<String, PackageMapping> = runOutageForBreakdown(
        previousDistribution = initialDistribution,
        brokenVehicleId = brokenVehicleId,
        remainingVehicles = remainingVehicles
    )

    // 6. Verify Assertions and Non-Migration Rules
    verifyAndOutputResults(
        initialDistribution = initialDistribution,
        updatedDistribution = updatedDistribution,
        brokenVehicleId = brokenVehicleId,
        expectedFallbackSlot = expectedFallbackVehicleSlot
    )
}

/**
 * Verifies Requirements #8 & #9:
 * - Cargo from broken vehicle is re-routed clockwise to next vehicle (Slot 65).
 * - Cargo from working vehicles (15, 65, 90) remains strictly non-migrated.
 */
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
            // Requirement 8 Assertion: Must shift to Slot 65
            check(newMapping.vehicleSlot == expectedFallbackSlot) {
                "Assertion Failure: Package $packageId was expected to shift to slot $expectedFallbackSlot but was mapped to ${newMapping.vehicleSlot}"
            }
            println("[RE-ROUTED] Package [$packageId] (Slot ${oldMapping.circleSlot}) -> Shifted from [${oldMapping.vehicleId}] to [${newMapping.vehicleId}] at Slot (${newMapping.vehicleSlot})")
            reroutedCargoCount++
        } else {
            // Requirement 9 Assertion: Must remain untouched
            check(oldMapping.vehicleId == newMapping.vehicleId && oldMapping.vehicleSlot == newMapping.vehicleSlot) {
                "Assertion Failure: Unaffected Package $packageId illegally migrated from ${oldMapping.vehicleId} to ${newMapping.vehicleId}"
            }
            println("[UNCHANGED] Package [$packageId] (Slot ${oldMapping.circleSlot}) -> Preserved on [${newMapping.vehicleId}] at Slot (${newMapping.vehicleSlot})")
            unaffectedCargoCount++
        }
    }

    println("\n==================================================")
    println("VERIFICATION COMPLETE & ASSERTIONS PASSED:")
    println("✓ Re-routed Packages (Broken $brokenVehicleId -> Slot $expectedFallbackSlot): $reroutedCargoCount")
    println("✓ Unaffected Packages Preserved (Slots 15, 65, 90): $unaffectedCargoCount")
    println("==================================================")
}

/**
 * Helper method to construct test package IDs.
 */
private fun generateSamplePackageIds(count: Int): List<String> {
    return (1..count).map { index -> "PKG-TEST-$index" }
}
