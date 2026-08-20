package domain.logic.algorithms.routing

import domain.model.Route
import domain.model.Warehouse

private const val INITIAL_DISTANCE = 0.0
private const val UNREACHABLE_DISTANCE = Double.MAX_VALUE

class ShortestDistanceRouteFinder (
    private val warehouses: List<Warehouse>,
    private val routingPathBuilder: RoutingPathBuilder
) : RouteFinder {

    private class DistanceSearchState {
        val unvisitedWarehouses = mutableSetOf<Warehouse>()
        val warehouseDistances = mutableMapOf<Warehouse, Double>()
        val previousWarehouses = mutableMapOf<Warehouse, Warehouse>()
    }

    override fun findRoute(
        startWarehouse: Warehouse,
        destinationWarehouse: Warehouse
    ): List<Warehouse> {

        if (startWarehouse == destinationWarehouse) {
            return listOf(startWarehouse)
        }

        val searchState = DistanceSearchState()
        initializeSearch(searchState, startWarehouse)
        return runDijkstraSearch(startWarehouse, destinationWarehouse, searchState)
    }

    private fun runDijkstraSearch(
        startWarehouse: Warehouse,
        destinationWarehouse: Warehouse,
        searchState: DistanceSearchState
    ): List<Warehouse> {

        while (searchState.unvisitedWarehouses.isNotEmpty()) {

            val currentWarehouse =
                findNearestUnvisitedWarehouse(searchState)
                    ?: return emptyList()

            if (currentWarehouse == destinationWarehouse) {
                return buildRoutePath(startWarehouse, destinationWarehouse, searchState)
            }

            searchState.unvisitedWarehouses.remove(currentWarehouse)
            updateConnectedWarehouseDistances(currentWarehouse, searchState)
        }

        return emptyList()
    }

    private fun initializeSearch(searchState: DistanceSearchState, startWarehouse: Warehouse) {
        for (warehouse in warehouses) {
            searchState.unvisitedWarehouses.add(warehouse)
            searchState.warehouseDistances[warehouse] = UNREACHABLE_DISTANCE
        }
        searchState.warehouseDistances[startWarehouse] = INITIAL_DISTANCE
    }

    private fun findNearestUnvisitedWarehouse(searchState: DistanceSearchState): Warehouse? {

        var nearestWarehouse: Warehouse? = null
        var shortestDistance = UNREACHABLE_DISTANCE

        for (warehouse in searchState.unvisitedWarehouses) {
            val warehouseDistance = searchState.warehouseDistances[warehouse] ?: INITIAL_DISTANCE

            if (warehouseDistance < shortestDistance) {
                shortestDistance = warehouseDistance
                nearestWarehouse = warehouse
            }
        }

        return nearestWarehouse
    }

    private fun updateConnectedWarehouseDistances(currentWarehouse: Warehouse, searchState: DistanceSearchState) {

        for (route in currentWarehouse.outgoingRoutes) {
            val nextWarehouse = route.destination
            if (nextWarehouse !in searchState.unvisitedWarehouses)  continue
              updateDistanceIfShorter(route,currentWarehouse,searchState)
        }
    }

    private fun updateDistanceIfShorter(
        route: Route,
        currentWarehouse: Warehouse,
        searchState: DistanceSearchState
    ) {

        val nextWarehouse = route.destination
        val currentDistance = searchState.warehouseDistances[currentWarehouse] ?: INITIAL_DISTANCE
        val newDistance = currentDistance + route.distanceKm
        val existingDistance = searchState.warehouseDistances[nextWarehouse] ?: INITIAL_DISTANCE

        if (newDistance < existingDistance) {
            searchState.warehouseDistances[nextWarehouse] = newDistance
            searchState.previousWarehouses[nextWarehouse] = currentWarehouse
        }
    }

    private fun buildRoutePath(
        startWarehouse: Warehouse,
        destinationWarehouse: Warehouse,
        searchState: DistanceSearchState
    ): List<Warehouse> {

        return routingPathBuilder.buildRoutePath(
            RoutePathRequest(
                startWarehouse = startWarehouse,
                destinationWarehouse = destinationWarehouse,
                previousWarehouses = searchState.previousWarehouses
            )
        )
    }
}