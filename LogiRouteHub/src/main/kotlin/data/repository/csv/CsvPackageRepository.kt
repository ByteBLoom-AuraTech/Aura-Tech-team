package data.repository.csv

import data.dataholders.PackageRaw
import data.processing.loaders.PackageLoader
import data.repository.PackageRepository

class CsvPackageRepository(
    private val loader: PackageLoader
) : PackageRepository {

    override fun getAll(): List<PackageRaw> {
        return loader.loadPackages()
    }
}