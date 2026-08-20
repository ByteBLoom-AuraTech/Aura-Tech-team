package domain.logic.algorithms.routing

import domain.model.Warehouse

data class RoutePathRequest(
    val startWarehouse: Warehouse,
    val destinationWarehouse: Warehouse,
    val previousWarehouses: Map<Warehouse, Warehouse>
)

class RoutingPathBuilder {
    fun buildRoutePath(routePathRequest: RoutePathRequest): List<Warehouse> {
        val routePath = mutableListOf<Warehouse>()
        var currentWarehouse = routePathRequest.destinationWarehouse

        while (
            currentWarehouse != routePathRequest.startWarehouse) {
            routePath.add(currentWarehouse)
            val previousWarehouse = routePathRequest.previousWarehouses[currentWarehouse] ?: return emptyList()
            currentWarehouse = previousWarehouse
        }

        routePath.add(routePathRequest.startWarehouse)
        return routePath.reversed()
    }
}