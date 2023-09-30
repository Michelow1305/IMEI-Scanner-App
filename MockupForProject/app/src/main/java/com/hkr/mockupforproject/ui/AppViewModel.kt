package com.hkr.mockupforproject.ui

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.hkr.mockupforproject.data.AppDatabase
import com.hkr.mockupforproject.data.AppRepository
import kotlinx.coroutines.launch

class AppViewModel(private val repository: AppRepository) : ViewModel() {

    var searchInfo by mutableStateOf(false)
    /*
    lateinit var db : AppDatabase


    fun init_db(context : Context){
        db = Room.databaseBuilder(context, AppDatabase::class.java, "app_database").build()
    }
    */

    fun initiateTowerCsvReading() {
        viewModelScope.launch {
            readTowerCsv()
        }
    }

    private suspend fun readTowerCsv() {
        repository.parseCellTowers()
    }

    companion object {
        fun create(context: Context): AppViewModel {
            val repository = AppRepository(context = context.applicationContext)
            return AppViewModel(repository)
        }
    }

}