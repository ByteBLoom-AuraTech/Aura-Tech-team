package domain.logic.algorithms

import domain.model.Warehouse

class BreadthFirstSearchRouter {

   fun findRoute(
     startWarehouse: Warehouse,
     destinationWarehouse: Warehouse
   ): List<Warehouse> {
        if (startWarehouse == destinationWarehouse){
            return listOf(startWarehouse)
        }

        val queue = ArrayDeque<Warehouse>()
        val visited = mutableSetOf<Warehouse>()
        val parent = mutableMapOf<Warehouse,Warehouse>()

        queue.addLast(startWarehouse)
        visited.add(startWarehouse)

        while (queue.isNotEmpty()){
          val currentWarehouse= queue.removeFirst()
          for(route in currentWarehouse.outgoingRoutes){
             val nextWarehouse = route.destination
              if (visitWarehouse(
                      nextWarehouse,
                      currentWarehouse,
                      destinationWarehouse,
                      queue,
                      visited,
                      parent)){
                  return buildPath(startWarehouse,destinationWarehouse,parent)
              }
          }
        }
        return emptyList()
   }

    private fun visitWarehouse(
        nextWarehouse: Warehouse,
        currentWarehouse: Warehouse,
        destinationWarehouse: Warehouse,
        queue: ArrayDeque<Warehouse>,
        visited: MutableSet<Warehouse>,
        parent: MutableMap<Warehouse,Warehouse>
    ): Boolean{
        if (nextWarehouse in visited){
             return false
        }
        visited.add(nextWarehouse)
        parent[nextWarehouse] = currentWarehouse
        if (nextWarehouse==destinationWarehouse){
            return true
        }
        queue.addLast(nextWarehouse)
        return false
    }

   private fun buildPath(
        startWarehouse: Warehouse,
        destinationWarehouse: Warehouse,
        parent: Map<Warehouse, Warehouse>
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