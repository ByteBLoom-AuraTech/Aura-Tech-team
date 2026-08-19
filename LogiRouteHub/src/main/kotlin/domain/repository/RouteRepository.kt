package domain.repository

import domain.model.Route


interface RouteRepository {
    fun getAll(): List<Route>
}