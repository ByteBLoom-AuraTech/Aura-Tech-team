package domain.repository
import domain.model.Package

interface PackageRepository {
    fun getAll(): List<Package>
}