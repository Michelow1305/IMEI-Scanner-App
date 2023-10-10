package com.hkr.mockupforproject.data

import androidx.lifecycle.LiveData
import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.TypeConverters
import androidx.room.Upsert

@Entity(tableName = "saved_device")
data class SavedDeviceData(
    @PrimaryKey @ColumnInfo(name = "imei") val imei: Int? = null,

    @ColumnInfo(name = "deviceDescription") val deviceDescription: String? = null,

    @ColumnInfo(name = "brand") val brand: String? = null,

    @ColumnInfo(name = "model") val model: String? = null,

    @ColumnInfo(name = "recommendation") val recommendation: String? = null,

    @ColumnInfo(name = "deviceName") val deviceName: String? = null,

    @ColumnInfo(name = "priority") val priority: Int? = null,

    @ColumnInfo(name = "latitude") val latitude: Float? = null,

    @ColumnInfo(name = "longitude") val longitude: Float? = null,

    @ColumnInfo (name = "checked") val checked: Boolean = false




) {
    override fun toString(): String {
        return "SavedDeviceData(imei=$imei, deviceDescription=$deviceDescription, brand=$brand," +
                "model=$model, recommendation=$recommendation,"+
                "deviceName=$deviceName, priority= $priority, checked= $checked )"
    }
}

@Dao
interface SavedDeviceDataDao {
    @Upsert
    fun insertDevice(device: SavedDeviceData)

    @Delete
    fun deleteDevice(device: SavedDeviceData)

    @Query("SELECT * FROM saved_device")
    fun allSavedDevices(): LiveData<List<SavedDeviceData>>

    @Query("DELETE FROM saved_device WHERE imei = :imeiNumb")
    fun devicesToDelete(imeiNumb: Int)


}


