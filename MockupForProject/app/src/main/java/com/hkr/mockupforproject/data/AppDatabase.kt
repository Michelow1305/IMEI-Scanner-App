package com.hkr.mockupforproject.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/*
    Add other Dao's to entities = []
 */
@Database(entities = [CellTower::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    /*
        This is where all the Dao's are placed (an interface with queries)
     */
    abstract fun cellTowerDao(): CellTowerDao


    /*
        This returns a Room database, it is a singleton to prevent multiple instances of the database at the same time.
     */
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {

                /*
                    Creates an instance of the built Room database.
                 */
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "app_database"
                ).build()

                INSTANCE = instance

                /*
                    Return the instance.
                 */
                instance
            }
        }
    }

}