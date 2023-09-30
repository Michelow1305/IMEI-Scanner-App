package com.hkr.mockupforproject.data

import android.content.Context
import androidx.annotation.WorkerThread
import androidx.room.Room
import com.hkr.mockupforproject.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.Reader

class AppRepository(context : Context, private val cellTowerDao: CellTowerDao) {
    val reader : Reader = context.resources.openRawResource(R.raw.data).reader()
    private val db: AppDatabase = AppDatabase.getDatabase(context)

//    suspend fun fetchData(): List<CellTower> {
//        return withContext(Dispatchers.IO) {
//            db.cellTowerDao().getAll()
//        }
//    }


    val allCellTowers : Flow<List<CellTower>> = cellTowerDao.getAll()

    @WorkerThread
    suspend fun addCellTowers(cellTowers : List<CellTower>){
        cellTowerDao.upsertAllCellTowers(cellTowers)
    }


    @WorkerThread
    suspend fun getCellTowersInRange(referenceLat: Float, referenceLon: Float, range: Int){
        cellTowerDao.getCellTowersInRange(referenceLat = referenceLat, referenceLon = referenceLon, range = range)
    }


    suspend fun parseCellTowers() {
        parseCSV(reader,db.cellTowerDao())
    }

    suspend fun clearTable() {
        cellTowerDao.clearTable()
    }
}