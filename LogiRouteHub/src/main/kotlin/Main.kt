package LogiRouteHub

import java.io.File

fun main() {

    val filePaths = listOf(
        "resources/packages.csv",
        "resources/warehouses.csv",
        "resources/routes.csv",
        "resources/fleet.csv"
    )
    val labels = listOf("Packages", "Warehouses", "Routes", "Fleet")

    val validCounts = mutableListOf<Int>()
    var packageLines = emptyList<String>()

    for (index in 0 until filePaths.size) {
        val path = filePaths[index]
        val file = File(path)
        val lines = if (file.exists()) file.readLines() else emptyList()

        if (index == 0) {
            packageLines = lines
        }

        var validSum = 0
        if (lines.isNotEmpty()) {
            val expectedCommas = countCommasForHeaders(lines[0])

            for (lineIndex in 0 until lines.size) {
                val line = lines[lineIndex]
                if (line.isNotBlank()) {
                    validSum += validateLineCommasCounter(line, lineIndex + 1, expectedCommas)
                }
            }
        }
        validCounts.add(validSum)
    }

    val rawEntities = processCsvFileLinesToEntities(packageLines, "PACKAGE")
    val packages = mutableListOf<PackageRaw>()

    for (i in 0 until rawEntities.size) {
        val entity = rawEntities[i]
        if (entity is PackageRaw) {
            packages.add(entity)
        }
    }

    val sortedPackages = sortPackagesUsingSelectionSort(packages)

    println("==========================================")
    println("Data Analysis Report (Exact count of successful records):")
    println("------------------------------------------")
    for (i in 0 until labels.size) {
        println("Number of ${labels[i]}: ${validCounts[i]}")
    }
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