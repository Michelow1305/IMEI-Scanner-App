package com.hkr.mockupforproject.data

import com.opencsv.CSVReader
import java.io.FileReader


const val MINIMUM_SAMPLES = 5
val MNClabels = arrayOf(
    "Direct2 Internet",
    "Telia",
    "Tre",
    "Netett Sverige AB",
    null,
    "Sweden 3G (Telia/Tele2)",
    "Telenor",
    "Tele2",
    "Telenor",
    "Djuice Mobile Sweden",
    "Spring",
    "Lindholmen Science Park",
    "Lycamobile",
    "Ventelo",
    "TDC",
    "Wireless Maingate",
    "42 Telecom AB",
    "Götalandsnätet AB",
    "Generic Mobile Systems Sweden AB",
    "Mudio Mobile",
    "Imez AB",
    null,
    "EuTel",
    "Infobip Ltd",
    null,
    "Digitel Mobile Srl",
    "Beepsend",
    "MyIndian AB",
    "CoolTEL Aps",
    "Mercury International Carrier Services",
    "NextGen Mobile Ltd",
    null,
    "CompaTel Ltd.",
    null,
    "Tigo LTD",
    null,
    "IDM",
    null,
    null,
    null,
    null,
    "Shyam Telecom UK Ltd",
    null,
    null,
    null,
    null,
    null,
    null,
    null,
    "ACN Communications Sweden AB",
    "89"
)


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
        val mnc = MNClabels[nextLine!![2].toInt()]
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
