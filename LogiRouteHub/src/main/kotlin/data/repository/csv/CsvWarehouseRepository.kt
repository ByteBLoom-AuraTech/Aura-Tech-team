package data.repository.csv

import data.dataholders.WarehouseRaw
import data.processing.loader.WarehouseLoader
import domain.repository.WarehouseRepository

class CsvWarehouseRepository(
    private val loader: WarehouseLoader
) : WarehouseRepository {

    override fun getAll(): List<WarehouseRaw> {
        return loader.loadWarehouses()
    }
}