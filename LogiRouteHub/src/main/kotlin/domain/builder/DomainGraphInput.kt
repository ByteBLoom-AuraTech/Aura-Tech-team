package domain.builder
import domain.repository.*

data class DomainGraphInput(
    val warehouseRepository: WarehouseRepository,
    val packageRepository: PackageRepository,
    val routeRepository: RouteRepository,
    val vehicleRepository: VehicleRepository
)