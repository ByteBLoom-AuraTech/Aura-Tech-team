package domain.logic.algorithms.routing

import domain.logic.algorithms.routing.LeastHopRouteFinder
import domain.logic.algorithms.routing.RouteFinder
import domain.logic.algorithms.routing.RoutingPathBuilder
import domain.logic.algorithms.routing.ShortestDistanceRouteFinder
import domain.model.Warehouse

class RouteFinderFactory (private val routingPathBuilder: RoutingPathBuilder) {
    fun createLeastHopRouteFinder(): RouteFinder {
        return LeastHopRouteFinder(
            routingPathBuilder = routingPathBuilder
        )
    }

    fun createShortestDistanceRouteFinder(warehouses: List<Warehouse>): RouteFinder {
        return ShortestDistanceRouteFinder(
            warehouses = warehouses,
            routingPathBuilder = routingPathBuilder
        )
    }

}
