package domain.logic.algorithms

import domain.model.Warehouse

interface Router {
    fun findRoute(
        startWarehouse: Warehouse,
        destinationWarehouse: Warehouse
    ): List<Warehouse>

    fun calculateHops(path: List<Warehouse>): Int = path.size - 1

    fun calculateTotalDistance(path: List<Warehouse>): Double {
        var totalDistance = 0.0
        for (i in 0 until path.size - 1) {
            val from = path[i]
            val to = path[i + 1]
            val route = from.outgoingRoutes.find { it.destination == to }
            totalDistance += route?.distanceKm ?: 0.0
        }
        return totalDistance
    }
}