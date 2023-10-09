package com.hkr.mockupforproject.data

data class SavedDevice(
    var deviceName: String = "Not defined",
    var deviceDescription: String = "Not defined",
    var brand: String = "Not defined",
    var model: String = "Not defined",
    var recommendation: String = "Not defined",
    var nearbyTowers: List<CellTower> = emptyList(),
    var imei: Long = 0,
    var priority: Int = 0
) {
    
}
