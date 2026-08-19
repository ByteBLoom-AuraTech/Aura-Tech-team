package domain.builder

import domain.model.*


data class DomainGraphData(
    val warehouses: List<Warehouse>,
    val packages: List<Package>,
    val vehicles: List<Vehicle>,
    val routes: List<Route>
)