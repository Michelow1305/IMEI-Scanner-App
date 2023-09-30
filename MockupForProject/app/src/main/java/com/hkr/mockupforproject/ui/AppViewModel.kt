package com.hkr.mockupforproject.ui

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.hkr.mockupforproject.data.AppDatabase
import com.hkr.mockupforproject.data.AppRepository
import kotlinx.coroutines.launch
class AppViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
     fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            return AppViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
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

}