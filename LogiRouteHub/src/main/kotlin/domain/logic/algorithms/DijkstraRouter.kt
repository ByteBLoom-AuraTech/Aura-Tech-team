package domain.logic.algorithms

import domain.model.Warehouse

class DijkstraRouter(
    private val allWarehouses: List<Warehouse>
) : Router {

    private class DijkstraSearchState(
        val startWarehouse: Warehouse,
        val allWarehouses: List<Warehouse>
    ) {
        val distances = mutableMapOf<Warehouse, Double>()
        val previous = mutableMapOf<Warehouse, Warehouse>()
        val unvisited = mutableSetOf<Warehouse>()

        init {
            allWarehouses.forEach { warehouse ->
                distances[warehouse] = Double.MAX_VALUE
                unvisited.add(warehouse)
            }
            distances[startWarehouse] = 0.0
        }

        fun findMinDistanceWarehouse(): Warehouse? {
            var minDistance = Double.MAX_VALUE
            var minWarehouse: Warehouse? = null

            for (warehouse in unvisited) {
                val distance = distances[warehouse] ?: Double.MAX_VALUE
                if (distance < minDistance) {
                    minDistance = distance
                    minWarehouse = warehouse
                }
            }

            return minWarehouse
        }

        fun updateNeighbor(
            current: Warehouse,
            neighbor: Warehouse,
            distance: Double
        ) {
            val newDistance = distances[current]!! + distance
            if (newDistance < distances[neighbor]!!) {
                distances[neighbor] = newDistance
                previous[neighbor] = current
            }
        }

        fun buildPath(destinationWarehouse: Warehouse): List<Warehouse> {
            val path = mutableListOf<Warehouse>()
            var current = destinationWarehouse

            while (current != startWarehouse) {
                path.add(current)
                current = previous[current] ?: return emptyList()
            }
            path.add(startWarehouse)

            return path.reversed()
        }
    }

    override fun findRoute(
        startWarehouse: Warehouse,
        destinationWarehouse: Warehouse
    ): List<Warehouse> {
        if (startWarehouse == destinationWarehouse) {
            return listOf(startWarehouse)
        }

        val searchState = DijkstraSearchState(startWarehouse, allWarehouses)

        while (searchState.unvisited.isNotEmpty()) {
            val currentWarehouse = searchState.findMinDistanceWarehouse()
                ?: break

            if (currentWarehouse == destinationWarehouse) {
                return searchState.buildPath(destinationWarehouse)
            }

            searchState.unvisited.remove(currentWarehouse)

            for (route in currentWarehouse.outgoingRoutes) {
                val neighbor = route.destination
                if (!searchState.unvisited.contains(neighbor)) continue

                searchState.updateNeighbor(
                    current = currentWarehouse,
                    neighbor = neighbor,
                    distance = route.distanceKm
                )
            }
        }

        return emptyList()
    }
}