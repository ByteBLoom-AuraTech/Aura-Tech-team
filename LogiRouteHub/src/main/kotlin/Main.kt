package LogiRouteHub

fun main() {
    val packageLines = readLinesFromCsvFile("src/main/resources/packages.csv")
    val warehouseLines = readLinesFromCsvFile("src/main/resources/warehouses.csv")
    val routeLines = readLinesFromCsvFile("src/main/resources/routes.csv")
    val fleetLines = readLinesFromCsvFile("src/main/resources/fleet.csv")

    val packages = processCsvFileLinesToEntities(packageLines, "PACKAGE").filterIsInstance<PackageRaw>()
    val warehouses = processCsvFileLinesToEntities(warehouseLines, "WAREHOUSE")
    val routes = processCsvFileLinesToEntities(routeLines, "ROUTE")
    val fleet = processCsvFileLinesToEntities(fleetLines, "FLEET")


    val sortedPackages = sortPackagesUsingSelectionSort(packages)fix: add missing src/main/ prefix to CSV resource paths

    println("==========================================")
    println("Data Analysis Report (Exact count of successful records):")
    println("------------------------------------------")
    println("Number of Packages: ${packages.size}")
    println("Number of Warehouses: ${warehouses.size}")
    println("Number of Routes: ${routes.size}")
    println("Number of Fleet: ${fleet.size}")
    println("==========================================\n")

    println("Top 3 packages by priority and weight (full details):")
    println("------------------------------------------")

    val top3Packages = sortedPackages.take(3)
    if (top3Packages.isEmpty()) {
        println("Not enough packages to display.")
    } else {
        for (i in 0 until top3Packages.size) {
            val pkg = top3Packages[i]
            println("${i + 1}. Package ID: ${pkg.id}")
            println("   Weight: ${pkg.weight}")
            println("   Destination: ${pkg.destinationHubId}")
            println("   Priority: ${pkg.priority}")
            println("------------------------------------------")
        }
    }
}