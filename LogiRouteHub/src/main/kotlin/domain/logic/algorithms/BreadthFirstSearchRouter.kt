package domain.logic.algorithms

import domain.model.Warehouse

class BreadthFirstSearchRouter : Router {

    private class BfsSearchState(
        val startWarehouse: Warehouse
    ) {
        val queue = ArrayDeque<Warehouse>()
        val visited = mutableSetOf<Warehouse>()
        val parent = mutableMapOf<Warehouse, Warehouse>()

        fun visitWarehouse(
            nextWarehouse: Warehouse,
            currentWarehouse: Warehouse
        ): Boolean {
            if (nextWarehouse in visited) {
                return false
            }
            visited.add(nextWarehouse)
            parent[nextWarehouse] = currentWarehouse
            return true
        }

        fun buildPath(
            destinationWarehouse: Warehouse
        ): List<Warehouse> {
            val path = mutableListOf<Warehouse>()
            var currentWarehouse = destinationWarehouse
            while (currentWarehouse != startWarehouse) {
                path.add(currentWarehouse)
                currentWarehouse = parent.getValue(currentWarehouse)
            }
            path.add(startWarehouse)
            return path.reversed()
        }
    }

    fun findRoute(
        startWarehouse: Warehouse,
        destinationWarehouse: Warehouse
    ): List<Warehouse> {
        if (startWarehouse == destinationWarehouse) {
            return listOf(startWarehouse)
        }
        val searchState = BfsSearchState(startWarehouse)
        searchState.queue.addLast(startWarehouse)
        searchState.visited.add(startWarehouse)

        while (searchState.queue.isNotEmpty()) {
            val currentWarehouse = searchState.queue.removeFirst()
            for (route in currentWarehouse.outgoingRoutes) {
                val nextWarehouse = route.destination
                when {
                    !searchState.visitWarehouse(nextWarehouse, currentWarehouse) -> {}
                    nextWarehouse == destinationWarehouse ->
                        return searchState.buildPath(destinationWarehouse)

                    else -> searchState.queue.addLast(nextWarehouse)
                }
            }
        }
        return emptyList()
    }
}
