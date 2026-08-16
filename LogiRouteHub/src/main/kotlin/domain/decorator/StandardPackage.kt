package LogiRouteHub.domain.decorator

import data.dataholders.PackageRaw


class StandardPackage (private val packageRaw: PackageRaw, private val baseRatePerKg: Double): PackageComponent {
    override fun calculateTransitRate(AuraFees: Double): Double {
        return packageRaw.weight * baseRatePerKg * AuraFees
    }
    override fun getDescription(): String {
        return "Standard Package"
    }
}