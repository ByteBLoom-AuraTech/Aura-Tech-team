package domain.repository
import domain.model.Vehicle

interface VehicleRepository {
    fun getAll(): List<Vehicle>
}