package com.hkr.mockupforproject.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/*
        src: https://wiki.opencellid.org/wiki/Menu_map_view#database
 */
@Entity
data class CellTower(
    /*
         This is a unique number used to identify each Base transceiver station or sector of BTS
     */
    @PrimaryKey val cid: Int? = null,

    /*
        The generation of broadband cellular network technology (Eg. LTE, GSM)
     */
    @ColumnInfo(name = "radio") val radio: String?,

    /*
        Mobile country code.
     */
    @ColumnInfo(name = "mcc") val mcc: Int?,

    /*
        Mobile network code
     */
    @ColumnInfo(name = "mnc") val mnc: Int?,

    /*
        Longitude, is a geographic coordinate that specifies the east-west position of a point on the Earth's surface
     */
    @ColumnInfo(name = "longitude") val longitude: Float?,

    /*
        Latitude is a geographic coordinate that specifies the north–south position of a point on the Earth's surface.
     */
    @ColumnInfo(name = "latitude") val latitude: Float?,

    /*
        Approximate area within which the cell could be. (In meters)
    */
    @ColumnInfo(name = "range") val range: Float?



) {
    override fun toString(): String {
        return "CellTower(cid=$cid, radio=$radio, mcc=$mcc, mnc=$mnc, longitude=$longitude, latitude=$latitude, range=$range)"
    }
}

