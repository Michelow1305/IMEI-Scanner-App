package com.hkr.mockupforproject.data

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class AppRepository(private val cellTowerDao: CellTowerDao) {
    val allCellTowers : Flow<List<CellTower>> = cellTowerDao.getAll()
    @WorkerThread
    suspend fun addCellTowers(cellTowers: List<CellTower>) {
        cellTowerDao.upsertAllCellTowers(cellTowers)
    }


    @WorkerThread
    suspend fun getCellTowersInRange(referenceLat: Float, referenceLon: Float, range: Int) {
        cellTowerDao.getCellTowersInRange(
            referenceLat = referenceLat,
            referenceLon = referenceLon,
            range = range
        )
    }


    @WorkerThread
    suspend fun clearTable() {
        cellTowerDao.clearTable()
    }

    @WorkerThread
    suspend fun findByCellTowerCid(cid: Int): CellTower? {
        return cellTowerDao.findByCid(60298)
    }

}