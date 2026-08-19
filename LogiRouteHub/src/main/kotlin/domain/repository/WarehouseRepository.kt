package domain.repository

import domain.model.Warehouse

interface WarehouseRepository {
    fun getAll(): List<Warehouse>
}