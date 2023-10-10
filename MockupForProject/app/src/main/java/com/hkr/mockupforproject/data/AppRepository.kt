package com.hkr.mockupforproject.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.Delete

class AppRepository(private val cellTowerDao: CellTowerDao, private val deviceDao: SavedDeviceDataDao ) {
    @WorkerThread
    fun getAll(): LiveData<List<CellTower>> {
        return cellTowerDao.getAll()
    }


    @WorkerThread
    suspend fun findByCid(cid: Int): CellTower? {
        return cellTowerDao.findByCid(cid)
    }


    @WorkerThread
    fun findByMnc(mnc: String): LiveData<List<CellTower>> {
        return cellTowerDao.findByMnc(mnc)
    }


    @WorkerThread
    suspend fun upsertCellTower(cellTower: CellTower) {
        cellTowerDao.upsertCellTower(cellTower)
    }


    @WorkerThread
    suspend fun upsertAllCellTowers(cellTowers: List<CellTower>) {
        cellTowerDao.upsertAllCellTowers(cellTowers)
    }


    @WorkerThread
    suspend fun clearTable() {
        cellTowerDao.clearTable()
    }


    @WorkerThread
    fun getCellTowersInRange(
        phoneLat: Float,
        phoneLon: Float,
        deltaLat: Float,
        deltaLon: Float
    ): LiveData<List<CellTower>> {
        return cellTowerDao.getCellTowersInRange(
            phoneLat = phoneLat,
            phoneLon = phoneLon,
            deltaLat = deltaLat,
            deltaLon = deltaLon
        )
    }
///////////////////////////////////////////////////////////////////////////////////
    //SavedDevices

    @WorkerThread
    suspend fun SavedDeviceGetAll(): LiveData<List<SavedDeviceData>> {
        return deviceDao.allSavedDevices()
    }

    @WorkerThread
    suspend fun upsertSavedDevice(savedDevice: SavedDeviceData) {
        deviceDao.insertDevice(savedDevice)
    }
    @WorkerThread
    fun deleteDevice(deleteDevice: SavedDeviceData){
        deviceDao.deleteDevice(deleteDevice)
    }

    fun devicesToDelete(imei: Int) {
         deviceDao.devicesToDelete(imei)
    }
}