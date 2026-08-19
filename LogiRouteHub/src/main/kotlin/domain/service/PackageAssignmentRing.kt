package algorithm

import kotlin.math.abs

data class RingVehicle(
    val id: String,
    val slot: Int
)

data class PackageMapping(
    val packageId: String,
    val circleSlot: Int,
    val vehicleId: String,
    val vehicleSlot: Int
) {
    override fun toString(): String {
        return "Package [$packageId] at Slot ($circleSlot) -> Assigned to Vehicle [$vehicleId] at Slot ($vehicleSlot)"
    }
}

object PackageAssignmentRing {

    const val CIRCLE_SIZE = 100

    const val VEHICLE_SLOT_A = 15
    const val VEHICLE_SLOT_B = 40
    const val VEHICLE_SLOT_C = 65
    const val VEHICLE_SLOT_D = 90

    val DEFAULT_VEHICLE_SLOTS = listOf(
        VEHICLE_SLOT_A,
        VEHICLE_SLOT_B,
        VEHICLE_SLOT_C,
        VEHICLE_SLOT_D
    )
}

fun mapPackageToSlot(packageId: String): Int {
    return abs(packageId.hashCode()) % PackageAssignmentRing.CIRCLE_SIZE
}

fun resolveVehicleClockwise(
    packageSlot: Int,
    vehicles: List<RingVehicle>
): RingVehicle {
    require(vehicles.isNotEmpty()) {
        "At least one vehicle must be available."
    }

    val sortedBySlot = vehicles.sortedBy { it.slot }

    return sortedBySlot.firstOrNull { it.slot >= packageSlot }
        ?: sortedBySlot.first()
}

fun distributeAllPackages(
    packageIds: List<String>,
    vehicles: List<RingVehicle>
): Map<String, PackageMapping> {
    return packageIds.associateWith { packageId ->
        val packageSlot = mapPackageToSlot(packageId)
        val assignedVehicle = resolveVehicleClockwise(packageSlot, vehicles)

        PackageMapping(
            packageId = packageId,
            circleSlot = packageSlot,
            vehicleId = assignedVehicle.id,
            vehicleSlot = assignedVehicle.slot
        )
    }
}

fun runOutageForBreakdown(
    previousDistribution: Map<String, PackageMapping>,
    brokenVehicleId: String,
    remainingVehicles: List<RingVehicle>
): Map<String, PackageMapping> {
    require(remainingVehicles.isNotEmpty()) {
        "At least one vehicle must remain active."
    }

    return previousDistribution.mapValues { (_, mapping) ->
        if (mapping.vehicleId == brokenVehicleId) {
            val newVehicle = resolveVehicleClockwise(
                mapping.circleSlot,
                remainingVehicles
            )

            mapping.copy(
                vehicleId = newVehicle.id,
                vehicleSlot = newVehicle.slot
            )
        } else {
            mapping
        }
    }
}