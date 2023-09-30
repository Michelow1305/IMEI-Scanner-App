package com.hkr.mockupforproject.data

import android.content.Context
import androidx.room.Room
import com.hkr.mockupforproject.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.Reader

class AppRepository(context: Context) {
    val reader : Reader = context.resources.openRawResource(R.raw.data).reader()
    private val db: AppDatabase = Room.databaseBuilder(context, AppDatabase::class.java, "app_database").build()

    suspend fun fetchData(): List<CellTower> {
        return withContext(Dispatchers.IO) {
            db.cellTowerDao().getAll()
        }
    }

    suspend fun parseCellTowers() {
        withContext(Dispatchers.IO) {
            parseCSV(reader,db.cellTowerDao())
        }
    }
}