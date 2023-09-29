package com.hkr.mockupforproject.data

import com.opencsv.CSVReader
import java.io.FileReader


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
