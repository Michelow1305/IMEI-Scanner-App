package com.hkr.mockupforproject.data

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.room.Room
import com.hkr.mockupforproject.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.Reader

class AppRepository(private val cellTowerDao: CellTowerDao) {
    val allCellTowers : Flow<List<CellTower>> = cellTowerDao.getAll()

    @WorkerThread
    suspend fun addCellTowers(cellTowers : List<CellTower>){
        cellTowerDao.upsertAllCellTowers(cellTowers)
    }


    @WorkerThread
    suspend fun getCellTowersInRange(referenceLat: Float, referenceLon: Float, range: Int){
        cellTowerDao.getCellTowersInRange(referenceLat = referenceLat, referenceLon = referenceLon, range = range)
    }

    suspend fun getAll(): Flow<List<CellTower>> {
        return cellTowerDao.getAll()
    }


    @WorkerThread
    suspend fun clearTable() {
        cellTowerDao.clearTable()
    }

    @WorkerThread
    suspend fun findByCellTowerCid(cid: Int) : CellTower? {
        return cellTowerDao.findByCid(60298)
    }

}