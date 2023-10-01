package com.hkr.mockupforproject.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

class AppRepository(private val cellTowerDao: CellTowerDao) {
    @WorkerThread
    suspend fun getAll() : List<CellTower>{
        return cellTowerDao.getAll()
    }


    @WorkerThread
    suspend fun findByCid(cid : Int) : CellTower? {
        return cellTowerDao.findByCid(cid)
    }


    @WorkerThread
    fun findByMnc(mnc : String) : LiveData<List<CellTower>> {
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
    suspend fun getCellTowersInRange(phoneLat: Float, phoneLon: Float, deltaLat : Float, deltaLon : Float): List<CellTower> {
        return cellTowerDao.getCellTowersInRange(
            phoneLat = phoneLat,
            phoneLon = phoneLon,
            deltaLat = deltaLat,
            deltaLon = deltaLon
        )
    }
}