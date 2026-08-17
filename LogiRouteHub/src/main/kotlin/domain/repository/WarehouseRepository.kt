package domain.repository

import data.dataholders.WarehouseRaw

interface WarehouseRepository {
    fun getAll(): List<WarehouseRaw>
}