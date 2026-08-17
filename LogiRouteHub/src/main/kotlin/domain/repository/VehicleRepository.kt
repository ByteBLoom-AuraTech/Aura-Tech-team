package domain.repository

import data.dataholders.FleetRaw

interface VehicleRepository {
    fun getAll(): List<FleetRaw>
}