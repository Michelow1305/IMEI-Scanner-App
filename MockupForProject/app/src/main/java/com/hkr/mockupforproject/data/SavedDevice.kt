package com.hkr.mockupforproject.data

import androidx.room.ColumnInfo

data class SavedDevice(
    var deviceName: String = "Not defined",
    var deviceDescription: String = "Not defined",
    var brand: String = "Not defined",
    var model: String = "Not defined",
    var recommendation: String = "Not defined",
    var nearbyTowers: List<CellTower> = emptyList(),
    var imei: Long = 0,
    var priority: Int = 0,
    var latitude: Float = 0F,
    var longitude: Float = 0F,
    val currentNetworkOperator: String = "Not defined",
    val currentNetworkType: String = "Not defined",
    val currentNetworkStrength: Int = 0,
    var supportedTechnologies: String = "NA",
    var checked: Boolean = false
) {
    
}
