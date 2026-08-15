package builder

import data.dataholders.FleetRaw
import data.dataholders.PackageRaw
import data.dataholders.RouteRaw
import data.dataholders.WarehouseRaw

data class RawGraphData(
    val warehouses: List<WarehouseRaw>,
    val packages: List<PackageRaw>,
    val vehicles: List<FleetRaw>,
    val routes: List<RouteRaw>
)