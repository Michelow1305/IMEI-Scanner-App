package com.hkr.mockupforproject.data

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

/*
        src: https://wiki.opencellid.org/wiki/Menu_map_view#database

        MNC codes to operator name: https://cellidfinder.com/mcc-mnc
 */
@Entity(tableName = "cell_towers", /*indices = [Index(value = ["longitude", "latitude"])]*/)
data class CellTower(
    /*
         This is a unique number used to identify each Base transceiver station or sector of BTS
     */
    @PrimaryKey @ColumnInfo(name = "cid") val cid: Int? = null,

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
    @ColumnInfo(name = "mnc") val mnc: String?,

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

@Dao
interface CellTowerDao {
    @Query("SELECT * FROM cell_towers")
    fun getAll(): Flow<List<CellTower>>

    @Transaction
    @Query("SELECT * FROM cell_towers WHERE cid = :cid")
    suspend fun findByCid(cid: Int): CellTower?

    @Transaction
    @Query("SELECT * FROM cell_towers WHERE mnc = :mnc")
    suspend fun findByMnc(mnc: Int): CellTower

    /*
        Upsert:
        If the passed cell tower already exists in the table, it will update it.
     */
    @Upsert
    suspend fun upsertCellTower(cellTower: CellTower)

    @Upsert
    suspend fun upsertAllCellTowers(cellTowers: List<CellTower>)

    @Query("DELETE FROM cell_towers")
    suspend fun clearTable()


    @Transaction
    @Query(
        """ SELECT * FROM cell_towers 
            WHERE latitude BETWEEN (:referenceLat - :range) AND (:referenceLat + :range)
            AND longitude BETWEEN (:referenceLon - :range) AND (:referenceLon + :range)
            """
    )
    suspend fun getCellTowersInRange(referenceLat: Float, referenceLon: Float, range: Int): List<CellTower>

}