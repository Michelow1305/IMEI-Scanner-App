package com.hkr.mockupforproject.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hkr.mockupforproject.data.AppRepository
import com.hkr.mockupforproject.data.CellTower
import kotlinx.coroutines.launch

class AppViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            return AppViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
class AppViewModel(private val repository: AppRepository) : ViewModel() {

    var searchInfo by mutableStateOf(false)
    var allCellTowers : LiveData<List<CellTower>> = repository.allCellTowers.asLiveData()


    fun addCellTowers(cellTowers : List<CellTower>) = viewModelScope.launch {
        repository.addCellTowers(cellTowers)
    }

    fun getCellTowersInRange(referenceLat: Float, referenceLon: Float, range: Int) = viewModelScope.launch {
        repository.getCellTowersInRange(referenceLat, referenceLon, range)
    }


}