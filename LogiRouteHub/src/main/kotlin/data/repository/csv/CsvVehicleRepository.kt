package data.repository.csv

import data.processing.loader.VehicleLoader
import domain.model.Vehicle
import domain.repository.VehicleRepository
import domain.repository.WarehouseRepository

class CsvVehicleRepository(
    private val loader: VehicleLoader,
    private val warehouseRepository: WarehouseRepository
) : VehicleRepository {

    override fun getAll(): List<Vehicle> {
        val warehouses = warehouseRepository.getAll()
        val warehouseIndex = warehouses.associateBy { it.id }

        return loader.loadFleets()
            .filter { fleetRaw ->
                warehouseIndex.containsKey(fleetRaw.currentHubId)
            }
            .flatMap { fleetRaw ->
                val currentHub = warehouseIndex.getValue(fleetRaw.currentHubId)

                fleetRaw.vehicleIds.map { vehicleId ->
                    Vehicle(
                        id = vehicleId,
                        maxCapacityKg = fleetRaw.maxCapacityKg,
                        costPerKm = fleetRaw.costPerKm,
                        currentHub = currentHub
                    )
                }
            }
    }
}