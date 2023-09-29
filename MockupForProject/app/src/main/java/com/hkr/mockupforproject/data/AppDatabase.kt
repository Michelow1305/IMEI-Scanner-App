package com.hkr.mockupforproject.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CellTower::class], version = 1)
abstract class AppDatabase : RoomDatabase(){
    abstract fun cellTowerDao() : CellTowerDao
}