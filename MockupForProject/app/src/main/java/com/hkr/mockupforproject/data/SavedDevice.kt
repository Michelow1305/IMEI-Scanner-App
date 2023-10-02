package com.hkr.mockupforproject.data

data class SavedDevice(
    val imei: Int,
    val deviceName: String,
    val deviceDescription: String,
    val brand: String,
    val model: String,
    val nearbyTowers: List<CellTower>,
    val priority: Int,
) {
    
}
