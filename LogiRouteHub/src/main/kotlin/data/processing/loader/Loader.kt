package data.processing

import data.dataholders.*
import data.processing.parser.*

fun loadPackages(): List<PackageRaw> {
    val lines = readCsvLines("packages.csv")
    return parsePackages(lines)
}

fun loadRoutes(): List<RouteRaw> {
    val lines = readCsvLines("routes.csv")
    return parseRoutes(lines)
}

fun loadFleets(): List<FleetRaw> {
    val lines = readCsvLines("fleet.csv")
    return parseFleet(lines)
}

fun loadWarehouses(): List<WarehouseRaw> {
    val lines = readCsvLines("warehouses.csv")
    return parseWarehouses(lines)
}

