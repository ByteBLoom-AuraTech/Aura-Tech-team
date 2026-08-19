package data.repository.csv

import data.processing.loader.RouteLoader
import domain.model.Route
import domain.repository.RouteRepository
import domain.repository.WarehouseRepository

class CsvRouteRepository(
    private val loader: RouteLoader,
    private val warehouseRepository: WarehouseRepository
) : RouteRepository {

    override fun getAll(): List<Route> {
        val warehouses = warehouseRepository.getAll()
        val warehouseIndex = warehouses.associateBy { it.id }

        return loader.loadRoutes()
            .filter { routeRaw ->
                warehouseIndex.containsKey(routeRaw.originHubId) &&
                        warehouseIndex.containsKey(routeRaw.destinationHubId)
            }
            .map { routeRaw ->
                Route(
                    id = routeRaw.id,
                    distanceKm = routeRaw.distanceKm,
                    typicalDelayMin = routeRaw.typicalDelayMin,
                    origin = warehouseIndex.getValue(routeRaw.originHubId),
                    destination = warehouseIndex.getValue(routeRaw.destinationHubId)
                )
            }
    }
}