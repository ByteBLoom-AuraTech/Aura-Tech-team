package data.repository

import data.dataholders.RouteRaw

interface RouteRepository {
    fun getAll(): List<RouteRaw>
}