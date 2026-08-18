package data.processing.loader

import data.dataholders.*
import data.processing.parser.*

class PackageLoader(private val fileName: String) {
    fun loadPackages(): List<PackageRaw> {
        val lines = readCsvLines(fileName)
        return parsePackages(lines)
    }
}

class RouteLoader(private val fileName: String) {
    fun loadRoutes(): List<RouteRaw> {
        val lines = readCsvLines(fileName)
        return parseRoutes(lines)
    }
}

class VehicleLoader(private val fileName: String) {
    fun loadFleets(): List<FleetRaw> {
        val lines = readCsvLines(fileName)
        return parseFleet(lines)
    }
}

class WarehouseLoader(private val fileName: String) {
    fun loadWarehouses(): List<WarehouseRaw> {
        val lines = readCsvLines(fileName)
        return parseWarehouses(lines)
    }
}
