package com.hkr.mockupforproject.ui

import androidx.annotation.WorkerThread
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hkr.mockupforproject.data.AppRepository
import com.hkr.mockupforproject.data.CellTower
import com.hkr.mockupforproject.data.deltaLat
import com.hkr.mockupforproject.data.deltaLon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            return AppViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

/*
    All queries are in a separate thread.
 */
class AppViewModel(private val repository: AppRepository) : ViewModel() {

    private var _allTowers : List<CellTower> = mutableListOf()
    private var _findByCidResult = MutableStateFlow(CellTower())
    private var _findByMncResult = MutableStateFlow(CellTower())
    private var _cellTowersInRange : List<CellTower> = mutableListOf()
    /*
        Accessible by the UI
     */
    val allTowers = _allTowers
    val findByCidResult = _findByCidResult.asStateFlow()
    val findByMncResult = _findByMncResult.asStateFlow()
    val cellTowersInRangeResult = _cellTowersInRange
    var searchInfo by mutableStateOf(false)

    fun getAll() = viewModelScope.launch(Dispatchers.IO) {
        _allTowers = repository.getAll()
    }


    fun findByCid(cid : Int) = viewModelScope.launch(Dispatchers.IO) {
        _findByCidResult.value = repository.findByCid(cid)!!

    }


    fun findByMnc(mnc : Int) = viewModelScope.launch(Dispatchers.IO) {
        _findByMncResult.value = repository.findByMnc(mnc)!!

    }

    fun upsertCellTower(cellTower: CellTower) = viewModelScope.launch(Dispatchers.IO) {
        repository.upsertCellTower(cellTower)

    }


    fun upsertAllCellTowers(cellTowers: List<CellTower>) = viewModelScope.launch(Dispatchers.IO) {
        repository.upsertAllCellTowers(cellTowers)

    }


    fun clearTable() = viewModelScope.launch(Dispatchers.IO) {
        repository.clearTable()

    }


    fun getCellTowersInRange(phoneLat: Float, phoneLon: Float) = viewModelScope.launch(Dispatchers.IO){
        _cellTowersInRange = repository.getCellTowersInRange(
            phoneLat = phoneLat,
            phoneLon = phoneLon,
            deltaLat = deltaLat(phoneLat).toFloat(),
            deltaLon = deltaLon(phoneLon, phoneLat).toFloat()
        )
    }

}