package algorithm

fun PackageAssignmentAudit() {
    val Vehicles = listOf(
        RingVehicle(id = "V15", slot = 15),
        RingVehicle(id = "V40", slot = 40),
        RingVehicle(id = "V65", slot = 65),
        RingVehicle(id = "V90", slot = 90)
    )
    val samplePackages = listOf("PKG-1", "PKG-2", "PKG-3", "PKG-4", "PKG-5")
    val initialDistribution = distributeAllPackages(samplePackages, Vehicles)
    val activeVehicles = Vehicles.filter { it.slot != 40 }
    val auditDistribution = runOutageForBreakdown(
        previousDistribution = initialDistribution,
        brokenVehicleId = "V40",
        remainingVehicles = activeVehicles
    )
    for (packageId in initialDistribution.keys) {
        val oldMapping = initialDistribution[packageId]!!
        val newMapping = auditDistribution[packageId]!!

        if (oldMapping.vehicleId == "V40") { check(newMapping.vehicleId == "V65") }
        else {check(newMapping.vehicleId == oldMapping.vehicleId) }
    }
}