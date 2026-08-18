package data.repository.csv

import data.dataholders.RouteRaw
import data.processing.loader.RouteLoader
import domain.repository.RouteRepository

class CsvRouteRepository(
    private val loader: RouteLoader
) : RouteRepository {

    override fun getAll(): List<RouteRaw> {
        return loader.loadRoutes()
    }
}