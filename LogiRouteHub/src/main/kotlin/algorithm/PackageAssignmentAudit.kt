package algorithm


fun auditAllPackages(
    previousDistribution: Map<String, PackageMapping>,
    currentDistribution: Map<String, PackageMapping>,
    brokenVehicleSlot: Int,
    nextVehicleSlot: Int
): Pair<Boolean, List<String>> {

    val auditLogs = mutableListOf<String>()
    var isSystemStable = true

    previousDistribution.forEach { (packageId, previous) ->
        val current = currentDistribution.getValue(packageId)
        if (previous.vehicleSlot == brokenVehicleSlot) {
            val isProperlyRerouted = current.vehicleSlot == nextVehicleSlot
            if (!isProperlyRerouted) {
                isSystemStable = false
            }

        } else if (previous.vehicleId != current.vehicleId) {
            isSystemStable = false
        }
    }
    return Pair(isSystemStable, auditLogs)
}