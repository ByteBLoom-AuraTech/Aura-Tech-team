package data.processing

import data.dataholders.FleetRaw
import data.dataholders.PackageRaw
import data.dataholders.RouteRaw
import data.dataholders.WarehouseRaw
import java.io.File

private const val RESOURCE_PATH = "src/main/resources2"

fun loadPackages(): List<PackageRaw> {
    val lines = readLines("packages.csv")
    return parsePackages(lines)
}

fun loadRoutes(): List<RouteRaw> {
    val lines = readLines("routes.csv")
    return parseRoutes(lines)
}

fun loadFleets(): List<FleetRaw> {
    val lines = readLines("fleet.csv")
    return parseFleet(lines)
}

fun loadWarehouses(): List<WarehouseRaw> {
    val lines = readLines("warehouses.csv")
    return parseWarehouses(lines)
}

private fun readLines(fileName: String): List<String> {
    return File("$RESOURCE_PATH/$fileName").readLines()
}