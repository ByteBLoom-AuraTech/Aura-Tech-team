package data.repository.csv

import data.processing.loader.PackageLoader
import domain.model.Package
import domain.model.Priority
import domain.repository.PackageRepository
import domain.repository.WarehouseRepository

class CsvPackageRepository(
    private val loader: PackageLoader,
    private val warehouseRepository: WarehouseRepository
) : PackageRepository {

    override fun getAll(): List<Package> {
        val warehouses = warehouseRepository.getAll()
        val warehouseIndex = warehouses.associateBy { it.id }

        return loader.loadPackages()
            .filter { packageRaw ->
                warehouseIndex.containsKey(packageRaw.originHubId) &&
                        warehouseIndex.containsKey(packageRaw.destinationHubId)
            }
            .map { packageRaw ->
                Package(
                    id = packageRaw.id,
                    weight = packageRaw.weight,
                    priority = Priority.valueOf(packageRaw.priority.name),
                    originHub = warehouseIndex.getValue(packageRaw.originHubId),
                    destinationHub = warehouseIndex.getValue(packageRaw.destinationHubId)
                )
            }
    }
}