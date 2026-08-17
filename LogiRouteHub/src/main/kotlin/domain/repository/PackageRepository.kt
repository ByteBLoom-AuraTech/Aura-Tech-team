package data.repository

import data.dataholders.PackageRaw

interface PackageRepository {
    fun getAll(): List<PackageRaw>
}