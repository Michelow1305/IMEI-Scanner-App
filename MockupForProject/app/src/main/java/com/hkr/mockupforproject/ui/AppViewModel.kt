package com.hkr.mockupforproject.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hkr.mockupforproject.R
import com.hkr.mockupforproject.data.AppRepository
import com.hkr.mockupforproject.data.CellTower
import kotlinx.coroutines.launch
import com.hkr.mockupforproject.data.parseCSV

class AppViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
//     fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
//            return AppViewModel(repository) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            return AppViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
class AppViewModel(private val repository: AppRepository) : ViewModel() {

    var searchInfo by mutableStateOf(false)
    val allCellTowers : LiveData<List<CellTower>> = repository.allCellTowers.asLiveData()


    /*
    lateinit var db : AppDatabase


    fun init_db(context : Context){
        db = Room.databaseBuilder(context, AppDatabase::class.java, "app_database").build()
    }
    */


    init {
        viewModelScope.launch {
            repository.clearTable()
            repository.parseCellTowers()
        }
    }

    fun addCellTowers(cellTowers : List<CellTower>) = viewModelScope.launch {
        repository.addCellTowers(cellTowers)
    }

    fun getCellTowersInRange(referenceLat: Float, referenceLon: Float, range: Int) = viewModelScope.launch {
        repository.getCellTowersInRange(referenceLat, referenceLon, range)
    }


//    fun initiateTowerCsvReading() {
//        viewModelScope.launch {
//            readTowerCsv()
//        }
//    }
//
//    private suspend fun readTowerCsv() {
//        repository.parseCellTowers()
//    }

}