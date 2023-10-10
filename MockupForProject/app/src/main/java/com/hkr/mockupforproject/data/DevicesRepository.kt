package com.hkr.mockupforproject.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.room.Delete
import androidx.room.Query

class DevicesRepository (private val deviceDao: SavedDeviceDataDao) {


    @WorkerThread
    fun allSavedDevices(): LiveData<List<SavedDeviceData>>{
        return deviceDao.allSavedDevices()

        /*
    @WorkerThread
    fun insetDevice(device: SavedDeviceData) {
        return deviceDao.insertDevice (device:)
    }

         */

}



}


