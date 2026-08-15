package data.repository.csv

import data.dataholders.FleetRaw
import data.processing.loaders.VehicleLoader
import data.repository.VehicleRepository

class CsvVehicleRepository(
    private val loader: VehicleLoader
) : VehicleRepository {

    override fun getAll(): List<FleetRaw> {
        return loader.loadFleets()
    }
}