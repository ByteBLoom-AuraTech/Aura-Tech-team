package builder

import domain.repository.PackageRepository
import domain.repository.RouteRepository
import domain.repository.VehicleRepository
import domain.repository.WarehouseRepository

data class DomainGraphInput(
    val warehouseRepository: WarehouseRepository,
    val packageRepository: PackageRepository,
    val routeRepository: RouteRepository,
    val vehicleRepository: VehicleRepository
)