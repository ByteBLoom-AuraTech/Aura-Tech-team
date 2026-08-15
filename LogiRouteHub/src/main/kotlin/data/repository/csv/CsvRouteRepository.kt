package data.repository.csv

import data.dataholders.RouteRaw
import data.processing.loaders.RouteLoader
import data.repository.RouteRepository

class CsvRouteRepository(
    private val loader: RouteLoader
) : RouteRepository {

    override fun getAll(): List<RouteRaw> {
        return loader.loadRoutes()
    }
}