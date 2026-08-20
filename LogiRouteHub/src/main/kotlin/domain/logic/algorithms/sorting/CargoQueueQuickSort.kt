package domain.logic.algorithms.sorting

import domain.model.Package

class CargoQueueQuickSort {

    companion object {
        private const val FIRST_INDEX = 0
        private const val ONE_ELEMENT = 1
    }

    fun sortPackagesByWeightDescending(packages: MutableList<Package>) {

        if (packages.isEmpty()) {
            return
        }
        quickSort(
            packages,
            FIRST_INDEX,
            packages.size - ONE_ELEMENT
        )
    }

    private fun quickSort(
        packages: MutableList<Package>,
        startIndex: Int,
        endIndex: Int
    ) {
        if (startIndex >= endIndex) {
            return
        }
        val pivotIndex = getPivotFinalPosition(
            packages,
            startIndex,
            endIndex
        )
        quickSort(
            packages,
            startIndex,
            pivotIndex - ONE_ELEMENT
        )
        quickSort(
            packages,
            pivotIndex + ONE_ELEMENT,
            endIndex
        )
    }

    private fun getPivotFinalPosition(
        packages: MutableList<Package>,
        startIndex: Int,
        endIndex: Int
    ): Int {
        val pivotWeight = packages[endIndex].weight
        var lastSortedIndex = startIndex

        for (currentIndex in startIndex until endIndex) {
            if (
                shouldMoveBeforePivot(
                    packages[currentIndex].weight,
                    pivotWeight
                )
            ) {
                swapPackages(
                    packages,
                    lastSortedIndex,
                    currentIndex
                )
                lastSortedIndex++
            }
        }
        swapPackages(
            packages,
            lastSortedIndex,
            endIndex
        )
        return lastSortedIndex
    }

    private fun shouldMoveBeforePivot(
        currentWeight: Double,
        pivotWeight: Double
    ): Boolean {
        return currentWeight > pivotWeight
    }

    private fun swapPackages(
        packages: MutableList<Package>,
        firstPackageIndex: Int,
        secondPackageIndex: Int
    ) {
        if (firstPackageIndex != secondPackageIndex) {
            val temporaryPackage = packages[firstPackageIndex]
            packages[firstPackageIndex] = packages[secondPackageIndex]
            packages[secondPackageIndex] = temporaryPackage
        }
    }
}