package domain.logic.algorithms.routing

import domain.model.Warehouse

class BidirectionalRouteFinder(
    warehouses: List<Warehouse>,
    private val routingPathBuilder: RoutingPathBuilder
) : RouteFinder {

        private val reverseAdjacency: Map<Warehouse, List<Warehouse>> =
            warehouses.flatMap { originWarehouse ->
                originWarehouse.outgoingRoutes.map { route -> route.destination to originWarehouse }
            }.groupBy({ it.first }, { it.second })

        private class BidirectionalSearchState(
            val startWarehouse: Warehouse,
            val destinationWarehouse: Warehouse
        ) {
            val forwardQueue = ArrayDeque<Warehouse>().apply { addLast(startWarehouse) }
            val forwardVisited = mutableSetOf(startWarehouse)
            val forwardParents = mutableMapOf<Warehouse, Warehouse>()

            val backwardQueue = ArrayDeque<Warehouse>().apply { addLast(destinationWarehouse) }
            val backwardVisited = mutableSetOf(destinationWarehouse)
            val backwardParents = mutableMapOf<Warehouse, Warehouse>()

            var warehousesEvaluated = 0
        }

        override fun findRoute(startWarehouse: Warehouse, destinationWarehouse: Warehouse): List<Warehouse> {
            if (startWarehouse == destinationWarehouse) {
                return listOf(startWarehouse)
            }

            val searchState = BidirectionalSearchState(startWarehouse, destinationWarehouse)
            val meetingWarehouse = searchForMeetingWarehouse(searchState)

            printEvaluationBenchmark(searchState, meetingWarehouse)

            if (meetingWarehouse == null) {
                return emptyList()
            }

            return buildUnifiedPath(searchState, meetingWarehouse)
        }

        private fun searchForMeetingWarehouse(searchState: BidirectionalSearchState): Warehouse? {
            while (searchState.forwardQueue.isNotEmpty() || searchState.backwardQueue.isNotEmpty()) {
                val forwardMeeting = expandForwardFrontier(searchState)
                if (forwardMeeting != null) return forwardMeeting

                val backwardMeeting = expandBackwardFrontier(searchState)
                if (backwardMeeting != null) return backwardMeeting
            }
            return null
        }

        private fun expandForwardFrontier(searchState: BidirectionalSearchState): Warehouse? {
            if (searchState.forwardQueue.isEmpty()) return null

            val currentWarehouse = searchState.forwardQueue.removeFirst()
            searchState.warehousesEvaluated++

            for (route in currentWarehouse.outgoingRoutes) {
                val nextWarehouse = route.destination
                if (nextWarehouse in searchState.forwardVisited) continue

                searchState.forwardVisited.add(nextWarehouse)
                searchState.forwardParents[nextWarehouse] = currentWarehouse
                searchState.forwardQueue.addLast(nextWarehouse)

                if (nextWarehouse in searchState.backwardVisited) {
                    return nextWarehouse
                }
            }
            return null
        }

        private fun expandBackwardFrontier(searchState: BidirectionalSearchState): Warehouse? {
            if (searchState.backwardQueue.isEmpty()) return null

            val currentWarehouse = searchState.backwardQueue.removeFirst()
            searchState.warehousesEvaluated++

            val predecessorWarehouses = reverseAdjacency[currentWarehouse].orEmpty()
            for (predecessorWarehouse in predecessorWarehouses) {
                if (predecessorWarehouse in searchState.backwardVisited) continue

                searchState.backwardVisited.add(predecessorWarehouse)
                searchState.backwardParents[predecessorWarehouse] = currentWarehouse
                searchState.backwardQueue.addLast(predecessorWarehouse)

                if (predecessorWarehouse in searchState.forwardVisited) {
                    return predecessorWarehouse
                }
            }
            return null
        }

        private fun buildUnifiedPath(
            searchState: BidirectionalSearchState,
            meetingWarehouse: Warehouse
        ): List<Warehouse> {
            val forwardSegment = routingPathBuilder.buildRoutePath(
                RoutePathRequest(
                    startWarehouse = searchState.startWarehouse,
                    destinationWarehouse = meetingWarehouse,
                    previousWarehouses = searchState.forwardParents
                )
            )

            val backwardSegment = routingPathBuilder.buildPathFollowingNextHops(
                NextHopPathRequest(
                    fromWarehouse = meetingWarehouse,
                    toWarehouse = searchState.destinationWarehouse,
                    nextHopWarehouses = searchState.backwardParents
                )
            )

            return forwardSegment + backwardSegment.drop(1)
        }

        private fun printEvaluationBenchmark(
            searchState: BidirectionalSearchState,
            meetingWarehouse: Warehouse?
        ) {
            val standardBfsEvaluations =
                countStandardBfsEvaluations(searchState.startWarehouse, searchState.destinationWarehouse)

            println("=== Bidirectional BFS vs Standard BFS Benchmark ===")
            println("Bidirectional BFS warehouses evaluated: ${searchState.warehousesEvaluated}")
            println("Standard BFS warehouses evaluated: $standardBfsEvaluations")

            if (meetingWarehouse == null) {
                println("No route found between ${searchState.startWarehouse.name} and ${searchState.destinationWarehouse.name}.")
            }
        }

        private fun countStandardBfsEvaluations(startWarehouse: Warehouse, destinationWarehouse: Warehouse): Int {
            val visitedWarehouses = mutableSetOf(startWarehouse)
            val warehouseQueue = ArrayDeque<Warehouse>().apply { addLast(startWarehouse) }
            var evaluatedCount = 0

            while (warehouseQueue.isNotEmpty()) {
                val currentWarehouse = warehouseQueue.removeFirst()
                evaluatedCount++

                if (currentWarehouse == destinationWarehouse) {
                    return evaluatedCount
                }

                for (route in currentWarehouse.outgoingRoutes) {
                    val nextWarehouse = route.destination
                    if (nextWarehouse !in visitedWarehouses) {
                        visitedWarehouses.add(nextWarehouse)
                        warehouseQueue.addLast(nextWarehouse)
                    }
                }
            }

            return evaluatedCount
        }
    }
