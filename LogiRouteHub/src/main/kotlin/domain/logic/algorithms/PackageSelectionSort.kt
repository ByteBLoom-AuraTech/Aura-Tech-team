package sorting


import data.dataholders.PackageRaw
import data.dataholders.PriorityRaw

fun getPriorityRank(priority: PriorityRaw): Int {
    return when (priority) {
        PriorityRaw.URGENT -> 3
        PriorityRaw.STANDARD -> 2
        PriorityRaw.LOW -> 1
    }
}

fun isHigherPackagePriority(firstPackage: PackageRaw, secondPackage: PackageRaw): Boolean {
    val firstPriorityRank = getPriorityRank(firstPackage.priority)
    val secondPriorityRank = getPriorityRank(secondPackage.priority)
    return firstPriorityRank > secondPriorityRank
}

fun isHigherPackageWeight(firstPackage: PackageRaw, secondPackage: PackageRaw): Boolean {
    return firstPackage.weight > secondPackage.weight
}

fun findBestPackageIndex(packages: List<PackageRaw>): Int {
    var bestPackageIndex = 0

    for (currentPackageIndex in 1 until packages.size) {
        val currentPackage = packages[currentPackageIndex]
        val selectedPackage = packages[bestPackageIndex]

        if (isHigherPackagePriority(currentPackage, selectedPackage)) {
            bestPackageIndex = currentPackageIndex
        } else if (currentPackage.priority == selectedPackage.priority && isHigherPackageWeight(currentPackage, selectedPackage)) {
            bestPackageIndex = currentPackageIndex
        }
    }

    return bestPackageIndex
}

fun sortPackagesUsingSelectionSort(packages: List<PackageRaw>): List<PackageRaw> {
    val unsortedPackages = packages.toMutableList()
    val sortedPackages = mutableListOf<PackageRaw>()

    while (unsortedPackages.isNotEmpty()) {
        val bestPackageIndex = findBestPackageIndex(unsortedPackages)

        sortedPackages.add(unsortedPackages[bestPackageIndex])
        unsortedPackages.removeAt(bestPackageIndex)
    }

    return sortedPackages
}