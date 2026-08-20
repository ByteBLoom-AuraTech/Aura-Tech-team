package domain.logic.algorithms.routing

import domain.model.Warehouse

interface RouteFinder {
    fun findRoute(
        startWarehouse: Warehouse,
        destinationWarehouse: Warehouse
    ): List<Warehouse>}