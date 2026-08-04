package LogiRouteHub
//
//<<<<<<< HEAD
//fun main() {
//    val packageLines = readLinesFromCsvFile("src/main/resources/packages.csv")
//    val warehouseLines = readLinesFromCsvFile("src/main/resources/warehouses.csv")
//    val routeLines = readLinesFromCsvFile("src/main/resources/routes.csv")
//    val fleetLines = readLinesFromCsvFile("src/main/resources/fleet.csv")
//
//    val packages = processCsvFileLinesToEntities(packageLines, "PACKAGE").filterIsInstance<PackageRaw>()
//    val warehouses = processCsvFileLinesToEntities(warehouseLines, "WAREHOUSE")
//    val routes = processCsvFileLinesToEntities(routeLines, "ROUTE")
//    val fleet = processCsvFileLinesToEntities(fleetLines, "FLEET")
//
//
//    val sortedPackages = sortPackagesUsingSelectionSort(packages)fix: add missing src/main/ prefix to CSV resource paths
//=======
//>>>>>>> 388f9d02962cdcd9235e2543fc80d55f9a274684

fun printReport(packages: List<PackageRaw>, warehouses: List<Any>, routes: List<Any>, fleet: List<Any>) {
    println("==========================================")
    println("Data Analysis Report (Exact count of successful records):")
    println("------------------------------------------")
    println("Number of Packages: ${packages.size}")
    println("Number of Warehouses: ${warehouses.size}")
    println("Number of Routes: ${routes.size}")
    println("Number of Fleet: ${fleet.size}")
    println("==========================================\n")
}

fun printTop3Packages(sortedPackages: List<PackageRaw>) {
    println("Top 3 packages by priority and weight (full details):")
    println("------------------------------------------")
    val limit = if (sortedPackages.size < 3) sortedPackages.size else 3
    if (sortedPackages.size == 0) {
        println("Not enough packages to display.")
        return
    }
    for (packageIndex in 0 until limit) {
        val pkg = sortedPackages[packageIndex]
        println("${packageIndex + 1}. Package ID: ${pkg.id}")
        println("   Weight: ${pkg.weight}")
        println("   Destination: ${pkg.destinationHubId}")
        println("   Priority: ${pkg.priority}")
        println("------------------------------------------")
    }
}

fun loadPackages(packageLines: List<String>): List<PackageRaw> {
    val rawPackages = processCsvFileLinesToEntities(packageLines, "PACKAGE")
    val packages = mutableListOf<PackageRaw>()
    for (packageIndex in 0 until rawPackages.size) {
        val entity = rawPackages[packageIndex]
        if (entity is PackageRaw) {
            packages.add(entity)
        }
    }
    return packages
}

fun main() {
    val packageLines = readLinesFromCsvFile("src/main/resources/packages.csv")
    val warehouseLines = readLinesFromCsvFile("src/main/resources/warehouses.csv")
    val routeLines = readLinesFromCsvFile("src/main/resources/routes.csv")
    val fleetLines = readLinesFromCsvFile("src/main/resources/fleet.csv")

    val packages = loadPackages(packageLines)
    val warehouses = processCsvFileLinesToEntities(warehouseLines, "WAREHOUSE")
    val routes = processCsvFileLinesToEntities(routeLines, "ROUTE")
    val fleet = processCsvFileLinesToEntities(fleetLines, "FLEET")

    val sortedPackages = sortPackagesUsingSelectionSort(packages)

    printReport(packages, warehouses, routes, fleet)
    printTop3Packages(sortedPackages)
}