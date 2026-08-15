package builder

import data.repository.PackageRepository
import data.repository.RouteRepository
import data.repository.VehicleRepository
import data.repository.WarehouseRepository

data class DomainGraphInput(
    val warehouseRepository: WarehouseRepository,
    val packageRepository: PackageRepository,
    val routeRepository: RouteRepository,
    val vehicleRepository: VehicleRepository
)