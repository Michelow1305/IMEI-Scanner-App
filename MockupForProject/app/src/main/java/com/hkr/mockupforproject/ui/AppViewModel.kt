package com.hkr.mockupforproject.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.hkr.mockupforproject.data.AppRepository
import com.hkr.mockupforproject.data.CellTower
import com.hkr.mockupforproject.data.SavedDevice
import com.hkr.mockupforproject.data.deltaLat
import com.hkr.mockupforproject.data.deltaLon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.hkr.mockupforproject.data.LocalDeviceInformation
import kotlinx.coroutines.launch

class AppViewModelFactory(
    private val repository: AppRepository,
    private val owner: LifecycleOwner
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AppViewModel::class.java)) {
            return AppViewModel(repository, owner) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}

/*
    All queries are run in a separate thread.
 */
open class AppViewModel(
    private val repository: AppRepository,
    private val owner: LifecycleOwner
) : ViewModel()
{
    var bottomSheetExpand by mutableStateOf(false)
    var bottomMenuOption by mutableIntStateOf(1)
    var localDeviceInformation : LocalDeviceInformation = LocalDeviceInformation()

    private lateinit var _allTowers: LiveData<List<CellTower>>
    private lateinit var _findByMncResult: LiveData<List<CellTower>>
    private lateinit var _cellTowersInRange: LiveData<List<CellTower>>
    private var _findByCidResult = MutableStateFlow(CellTower())


    /*
        For scanning IMEI -
        TODO: Delete.
     */
    private val _scannedImeis = MutableLiveData<List<Long>>()
    val scannedImeis: LiveData<List<Long>> get() = _scannedImeis


    /*
        Accessible by the UI
     */
    val allTowers: LiveData<List<CellTower>> get() = _allTowers
    val findByMncResult: LiveData<List<CellTower>> get() = _findByMncResult
    val cellTowersInRangeResult: LiveData<List<CellTower>> get() = _cellTowersInRange
    val findByCidResult = _findByCidResult.asStateFlow()
    var cellTowersInRangeHasTowers by mutableStateOf(false)
    var searchInfo by mutableStateOf(false)


    fun addScannedImeis(newImeis: List<Long>) {
        val currentImeis = _scannedImeis.value ?: emptyList()
        val uniqueImeis = newImeis.filter { it !in currentImeis }
        _scannedImeis.value = currentImeis + uniqueImeis
        
        if(currentImeis.isNotEmpty()){
            currentDeviceToSave = SavedDevice(imei = currentImeis[0])
        }

    }



    // Current Device to save
    var currentDeviceToSave: SavedDevice = SavedDevice()

    fun getAll() = viewModelScope.launch(Dispatchers.IO) {
        _allTowers = repository.getAll()
    }


    fun findByCid(cid: Int) = viewModelScope.launch(Dispatchers.IO) {
        _findByCidResult.value = repository.findByCid(cid)!!
    }


    fun findByMnc(mnc: String) = viewModelScope.launch(Dispatchers.IO) {
        _findByMncResult = repository.findByMnc(mnc)
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


    fun getCellTowersInRange(phoneLat: Float, phoneLon: Float) =
        viewModelScope.launch(Dispatchers.IO) {
            _cellTowersInRange = (repository.getCellTowersInRange(
                phoneLat = phoneLat,
                phoneLon = phoneLon,
                deltaLat = deltaLat(phoneLat).toFloat(),
                deltaLon = deltaLon(phoneLon, phoneLat).toFloat()
            ))
        }

    fun InRangeHasTowers() {
        cellTowersInRangeResult.observe(
            owner,
            Observer { value -> cellTowersInRangeHasTowers = true })
    }


    // This will notify the Activity, which is observing this LiveData, to request permissions
    /*
    Function name:	checkAndAskPermission()
    Inputs:			NA
    Outputs:		requestPermission is observed by MainActivity. When function is called MainActivity will react
    Called by:		MainActivity
    Calls:			NA
    Author:         Joel Andersson
     */
    val requestPermission = MutableLiveData<Unit>()
    fun checkAndAskPermission() {
        requestPermission.value = Unit
    }

    fun hasReadPhoneStatePermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun hasReadPhoneLocationPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun hasCameraPermission(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

    }



}