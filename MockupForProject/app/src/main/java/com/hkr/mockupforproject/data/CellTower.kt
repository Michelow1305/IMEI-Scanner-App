package com.hkr.mockupforproject.data

import com.opencsv.CSVReader
import java.io.FileReader

data class CellTower(
    val radio: String? = null,
    val mcc: Int? = null,
    val mnc: Int? = null,
    val cid: Int? = null,
    val longitude: Float? = null,
    val latitude: Float? = null,
    /*
        Approximate area within which the cell could be. (In meters)
        src: https://wiki.opencellid.org/wiki/Menu_map_view#database
     */
    val range: Float? = null,
) {
    override fun toString(): String {
        return "CellTower(radio=$radio, mcc=$mcc, mnc=$mnc, cid=$cid, longitude=$longitude, latitude=$latitude, range=$range)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as CellTower

        if (radio != other.radio) return false
        if (mcc != other.mcc) return false
        if (mnc != other.mnc) return false
        if (cid != other.cid) return false
        if (longitude != other.longitude) return false
        if (latitude != other.latitude) return false
        if (range != other.range) return false

        return true
    }

    override fun hashCode(): Int {
        var result = radio?.hashCode() ?: 0
        result = 31 * result + (mcc ?: 0)
        result = 31 * result + (mnc ?: 0)
        result = 31 * result + (cid ?: 0)
        result = 31 * result + (longitude?.hashCode() ?: 0)
        result = 31 * result + (latitude?.hashCode() ?: 0)
        result = 31 * result + (range?.hashCode() ?: 0)
        return result
    }

}

const val MINIMUM_SAMPLES = 5

fun main() {
    val csvFilePath = "app\\src\\main\\java\\com\\hkr\\mockupforproject\\data\\240.csv"

    val cells = parseCSV(csvFilePath)

    cells.forEach { i ->
        println(i)
    }

}

fun parseCSV(path: String): List<CellTower> {
    val reader = CSVReader(FileReader(path))
    val csvObjects = mutableListOf<CellTower>()
    var nextLine: Array<String>?

    while (reader.readNext().also { nextLine = it } != null) {
        val radio = nextLine!![0]
        val mcc = nextLine!![1].toInt()
        val mnc = nextLine!![2].toInt()
        val cid = nextLine!![4].toInt()
        val longitude = nextLine!![5].toFloat()
        val latitude = nextLine!![6].toFloat()
        val range = nextLine!![7].toFloat()
        val samples = nextLine?.get(8)?.toInt()

        if (samples != null && samples < MINIMUM_SAMPLES && radio != "LTE") {
            nextLine = reader.readNext()
        }

        csvObjects.add(
            CellTower(
                radio = radio,
                mcc = mcc,
                mnc = mnc,
                cid = cid,
                longitude = longitude,
                latitude = latitude,
                range = range,
            )
        )
    }

    return csvObjects
}
