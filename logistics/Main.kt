package logistics

import java.io.File
import logistics.Dataholder.*
import logistics.parsers.*
import logistics.sorting.*

fun main() {
    // 1. Define file paths (paths prepared by Fatima)
    val packagesFile = File("C:/Users/user/Desktop/kotiinproject/data/packages.csv")
    val warehousesFile = File("C:/Users/user/Desktop/kotiinproject/data/warehouses.csv")
    val routesFile = File("C:/Users/user/Desktop/kotiinproject/data/routes.csv")
    val fleetFile = File("C:/Users/user/Desktop/kotiinpro+ject/data/fleet.csv")

    // 2. Read all lines from the CSV files
    val packageLines = if (packagesFile.exists()) packagesFile.readLines() else emptyList()
    val warehouseLines = if (warehousesFile.exists()) warehousesFile.readLines() else emptyList()
    val routeLines = if (routesFile.exists()) routesFile.readLines() else emptyList()
    val fleetLines = if (fleetFile.exists()) fleetFile.readLines() else emptyList()

    // 3. Parse the data and call the cleaning functions (implemented by Fatima)
    // This step filters out failed/incomplete/empty/header rows
    val packages = parsePackageCsv(packageLines).toMutableList()
    val warehouses = parseWarehouseCsv(warehouseLines)
    val routes = parseRouteCsv(routeLines)
    val fleet = parseFleetCsv(fleetLines)

    // 4. Sort packages by priority and weight (call the sorting function)
    sortPackagesByPriorityAndWeight(packages)

    // --- [Your task - Request 1]: Print the exact count of successfully parsed records ---
    println("==========================================")
    println("Data Analysis Report (Exact count of successful records):")
    println("------------------------------------------")
    println("Number of Packages: ${packages.size}")
    println("Number of Warehouses: ${warehouses.size}")
    println("Number of Routes: ${routes.size}")
    println("Number of Fleet: ${fleet.size}")
    println("==========================================\n")

    // --- [Your task - Request 2]: Print the full details of the top 3 packages after sorting ---
    println("Top 3 packages by priority and weight (full details):")
    println("------------------------------------------")

    val top3Packages = packages.take(3)

    if (top3Packages.isEmpty()) {
        println("Not enough packages to display.")
    } else {
        top3Packages.forEachIndexed { index, pkg ->
            println("${index + 1}. Package ID: ${pkg.id}")
            println("   Weight: ${pkg.weight}")
            println("   Destination: ${pkg.destinationHubId}")
            println("   Priority: ${pkg.priority}")
            println("------------------------------------------")
        }
    }
}
