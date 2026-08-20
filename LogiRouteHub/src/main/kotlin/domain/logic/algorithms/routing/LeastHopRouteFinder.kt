package domain.logic.algorithms.routing

import domain.model.Warehouse

class LeastHopRouteFinder(
    private val routingPathBuilder: RoutingPathBuilder
) : RouteFinder {

    private class RouteSearchState {
        val warehouseQueue = ArrayDeque<Warehouse>()
        val visitedWarehouses = mutableSetOf<Warehouse>()
        val previousWarehouses = mutableMapOf<Warehouse, Warehouse>()
    }

    override fun findRoute(startWarehouse: Warehouse, destinationWarehouse: Warehouse): List<Warehouse> {

        if (startWarehouse == destinationWarehouse) {
            return listOf(startWarehouse)
        }
        val searchState = RouteSearchState()
        initializeSearch(searchState, startWarehouse)

        while (searchState.warehouseQueue.isNotEmpty()) {
            val currentWarehouse = getNextWarehouse(searchState)
            if (exploreNextWarehouses(
                    currentWarehouse, destinationWarehouse, searchState
                )
            ) {
                return buildRoutePath(
                    startWarehouse, destinationWarehouse, searchState
                )
            }
        }
        return emptyList()
    }

    private fun initializeSearch(
        searchState: RouteSearchState, startWarehouse: Warehouse
    ) {
        searchState.warehouseQueue.addLast(startWarehouse)
        searchState.visitedWarehouses.add(startWarehouse)
    }

    private fun getNextWarehouse(searchState: RouteSearchState): Warehouse {
        return searchState.warehouseQueue.removeFirst()
    }

    private fun exploreNextWarehouses(
        currentWarehouse: Warehouse, destinationWarehouse: Warehouse, searchState: RouteSearchState
    ): Boolean {

        for (route in currentWarehouse.outgoingRoutes) {
            val nextWarehouse = route.destination
            if (isWarehouseAlreadyVisited(nextWarehouse, searchState)
            ) {
                continue
            }

            visitWarehouse(currentWarehouse, nextWarehouse, searchState)
            if (nextWarehouse == destinationWarehouse) {
                return true
            }
            searchState.warehouseQueue.addLast(nextWarehouse)
        }

        return false
    }

    private fun isWarehouseAlreadyVisited(
        warehouse: Warehouse, searchState: RouteSearchState
    ): Boolean {
        return warehouse in searchState.visitedWarehouses
    }

    private fun visitWarehouse(currentWarehouse: Warehouse, nextWarehouse: Warehouse, searchState: RouteSearchState) {
        searchState.visitedWarehouses.add(nextWarehouse)
        searchState.previousWarehouses[nextWarehouse] = currentWarehouse
    }

    private fun buildRoutePath(
        startWarehouse: Warehouse,
        destinationWarehouse: Warehouse,
        searchState: RouteSearchState
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