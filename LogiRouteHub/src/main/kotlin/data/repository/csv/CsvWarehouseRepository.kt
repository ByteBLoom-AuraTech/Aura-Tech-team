package data.repository.csv

import data.processing.loader.WarehouseLoader
import domain.model.Warehouse
import domain.repository.WarehouseRepository

class CsvWarehouseRepository(
    private val loader: WarehouseLoader
) : WarehouseRepository {

    override fun getAll(): List<Warehouse> {
        return loader.loadWarehouses().map { warehouseRaw ->
            Warehouse(
                id = warehouseRaw.id,
                name = warehouseRaw.name,
                regionalZone = warehouseRaw.regionalZone
            )
        }
    }
}