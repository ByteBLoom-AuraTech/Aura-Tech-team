package data.repository.csv

import data.processing.loader.WarehouseLoader
import domain.model.Warehouse
import domain.repository.WarehouseRepository

class CsvWarehouseRepository(private val loader: WarehouseLoader) : WarehouseRepository {
    private var warehouses: List<Warehouse>? = null
    override fun getAll(): List<Warehouse> {
        if (warehouses == null) {
            warehouses = loadWarehouses()
        }
        return warehouses.orEmpty()
    }
    private fun loadWarehouses(): List<Warehouse> {
        return loader.loadWarehouses().map { warehouseRaw ->
            Warehouse(
                id = warehouseRaw.id,
                name = warehouseRaw.name,
                regionalZone = warehouseRaw.regionalZone
            )
        }
    }
}