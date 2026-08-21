import data.processing.loader.*
import data.repository.csv.*
import domain.builder.DomainGraphBuilder
import domain.builder.DomainGraphInput
import domain.logic.algorithms.routing.*
import domain.logic.algorithms.sorting.sortPackagesUsingSelectionSort
import domain.logic.pricing.additionalpricing.*
import domain.logic.pricing.basepricing.*
import domain.logic.pricing.config.*
import domain.model.Warehouse
import domain.repository.*

private const val WAREHOUSES_FILE_NAME = "warehouses.csv"
private const val PACKAGES_FILE_NAME = "packages.csv"
private const val ROUTES_FILE_NAME = "routes.csv"
private const val FLEET_FILE_NAME = "fleet.csv"
private const val DEMO_BASE_TRANSIT_RATE = 100.0
private const val TOP_PRIORITY_COUNT = 3

fun main() {
    printSortedPriorityPackages()

    val warehouses = buildDomainGraph()

    sortAndPrintCargoQueue(warehouses)
    switchPricingStrategy()

    val routeFinderFactory = RouteFinderFactory(RoutingPathBuilder())
    printDecoratedPackageRate(warehouses)
    printRoutingComparison(warehouses, routeFinderFactory)
    printBidirectionalRouting(warehouses, routeFinderFactory)

}

// --------------------------------------------------------------
// CSV parsing pipeline + manual Selection Sort on raw packages
// --------------------------------------------------------------

private fun printSortedPriorityPackages() {
    val rawPackages = PackageLoader(PACKAGES_FILE_NAME).loadPackages()
    val sortedPackages = sortPackagesUsingSelectionSort(rawPackages)

    println("Successfully parsed packages: ${rawPackages.size}")
    println("Top $TOP_PRIORITY_COUNT priority packages:")
    sortedPackages.take(TOP_PRIORITY_COUNT).forEach { println(it) }
}

// --------------------------------------------------------------
// Domain graph construction (repositories -> builder -> warehouses)
// --------------------------------------------------------------

private fun buildDomainGraph(): List<Warehouse> {
    val warehouseLoader = WarehouseLoader(WAREHOUSES_FILE_NAME)
    val packageLoader = PackageLoader(PACKAGES_FILE_NAME)
    val routeLoader = RouteLoader(ROUTES_FILE_NAME)
    val vehicleLoader = VehicleLoader(FLEET_FILE_NAME)

    val warehouseRepository: WarehouseRepository =
        CsvWarehouseRepository(warehouseLoader)

    val packageRepository: PackageRepository =
        CsvPackageRepository(packageLoader, warehouseRepository)

    val routeRepository: RouteRepository =
        CsvRouteRepository(routeLoader, warehouseRepository)

    val vehicleRepository: VehicleRepository =
        CsvVehicleRepository(vehicleLoader, warehouseRepository)

    val domainGraphInput = DomainGraphInput(
        warehouseRepository = warehouseRepository,
        packageRepository = packageRepository,
        routeRepository = routeRepository,
        vehicleRepository = vehicleRepository
    )

    return DomainGraphBuilder(domainGraphInput).buildGraph()
}

private fun sortAndPrintCargoQueue(warehouses: List<Warehouse>) {
    val firstWarehouse = warehouses.firstOrNull() ?: run {
        println("No warehouse available to demonstrate sorting.")
        return
    }

    firstWarehouse.sortCargoQueue()

    println("Sorted cargo queue for ${firstWarehouse.name}:")
    firstWarehouse.cargoQueue.forEach { println(it) }
}

private fun switchPricingStrategy() {
    val basePricingConfig = BasePricingConfig()
    val routePricingEngine = RoutePricingEngine(EcoStrategy(basePricingConfig))

    println("Strategy switched from Eco to Express")
    routePricingEngine.switchStrategy(ExpressStrategy(basePricingConfig))
}

// --------------------------------------------------------------
// Decorator pattern
// --------------------------------------------------------------

private fun printDecoratedPackageRate(warehouses: List<Warehouse>) {
    val firstPackage: PackageComponent =
        warehouses.firstOrNull()?.cargoQueue?.firstOrNull() ?: run {
            println("No package available to demonstrate decorators.")
            return
        }

    val additionalPricingConfig = AdditionalPricingConfig()

    val decoratedPackage = ExpressInsuranceDecorator(
        packageComponent = ColdChainDecorator(
            packageComponent = FragileHandlingDecorator(firstPackage, additionalPricingConfig),
            additionalPricingConfig = additionalPricingConfig
        ),
        additionalPricingConfig = additionalPricingConfig
    )

    val transitRate = decoratedPackage.calculateTransitRate(
        baseTransitRate = DEMO_BASE_TRANSIT_RATE,
        auraFees = additionalPricingConfig.auraFees
    )

    println("Decorated package rate: $transitRate")
}

// --------------------------------------------------------------
// BFS  and Dijkstra routing
// --------------------------------------------------------------

private fun printRoutingComparison(
    warehouses: List<Warehouse>,
    routeFinderFactory: RouteFinderFactory
) {
    if (warehouses.size < 2) {
        println("Not enough warehouses to demonstrate routing.")
        return
    }

    val startWarehouse = warehouses.first()
    val destinationWarehouse = warehouses.last()

    val leastHopRouteFinder: RouteFinder = routeFinderFactory.createLeastHopRouteFinder()
    val shortestDistanceRouteFinder: RouteFinder =
        routeFinderFactory.createShortestDistanceRouteFinder(warehouses)

    val leastHopPath = leastHopRouteFinder.findRoute(startWarehouse, destinationWarehouse)
    val shortestDistancePath =
        shortestDistanceRouteFinder.findRoute(startWarehouse, destinationWarehouse)

    println("BFS (Least Hops): " + leastHopPath.joinToString(" -> ") { it.name })
    println("Dijkstra (Shortest Distance): " + shortestDistancePath.joinToString(" -> ") { it.name })
}

// --------------------------------------------------------------
// ========== BFS vs BIDIRECTIONAL BFS ==========
// --------------------------------------------------------------
private fun printBidirectionalRouting(
    warehouses: List<Warehouse>,
    routeFinderFactory: RouteFinderFactory) {

    if (warehouses.size < 2) {
        println("Not enough warehouses to demonstrate bidirectional routing.")
        return
    }

    val startWarehouse = warehouses.first()
    val destinationWarehouse = warehouses.last()

    val bidirectionalRouteFinder: RouteFinder =
        routeFinderFactory.createBidirectionalRouteFinder(warehouses)

    val bidirectionalPath = bidirectionalRouteFinder.findRoute(startWarehouse, destinationWarehouse)

    println("Bidirectional BFS path: " + bidirectionalPath.joinToString(" -> ") { it.name })
}
