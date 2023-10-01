package com.hkr.mockupforproject.data

import com.opencsv.CSVReader
import java.io.Reader


const val MINIMUM_SAMPLES = 1
val providers = arrayOf(
    "Direct2 Internet",
    "Telia",
    "Tre",
    "Netett Sverige AB",
    null,  // Index 4 is missing, so we use null as a placeholder
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
    null,  // Index 21 is missing, so we use null as a placeholder
    "EuTel",
    "Infobip Ltd",
    null,  // Index 24 is missing, so we use null as a placeholder
    "Digitel Mobile Srl",
    "Beepsend",
    "MyIndian AB",
    "CoolTEL Aps",
    "Mercury International Carrier Services",
    "NextGen Mobile Ltd",
    null,  // Index 31 is missing, so we use null as a placeholder
    "CompaTel Ltd.",
    null,  // Index 33 is missing, so we use null as a placeholder
    "Tigo LTD",
    null,  // Index 35 is missing, so we use null as a placeholder
    "IDM",
    null,  // Index 37 is missing, so we use null as a placeholder
    null,  // Index 38 is missing, so we use null as a placeholder
    null,  // Index 39 is missing, so we use null as a placeholder
    null,  // Index 40 is missing, so we use null as a placeholder
    "Shyam Telecom UK Ltd",
    null,  // Index 42 is missing, so we use null as a placeholder
    null,  // Index 43 is missing, so we use null as a placeholder
    null,  // Index 44 is missing, so we use null as a placeholder
    null,  // Index 45 is missing, so we use null as a placeholder
    null,  // Index 46 is missing, so we use null as a placeholder
    null,  // Index 47 is missing, so we use null as a placeholder
    null,  // Index 48 is missing, so we use null as a placeholder
    null,  // Index 49 is missing, so we use null as a placeholder
    null,  // Index 50 is missing, so we use null as a placeholder
    null,  // Index 51 is missing, so we use null as a placeholder
    null,  // Index 52 is missing, so we use null as a placeholder
    null,  // Index 53 is missing, so we use null as a placeholder
    null,  // Index 54 is missing, so we use null as a placeholder
    null,  // Index 55 is missing, so we use null as a placeholder
    null,  // Index 56 is missing, so we use null as a placeholder
    null,  // Index 57 is missing, so we use null as a placeholder
    null,  // Index 58 is missing, so we use null as a placeholder
    null,  // Index 59 is missing, so we use null as a placeholder
    null,  // Index 60 is missing, so we use null as a placeholder
    null,  // Index 61 is missing, so we use null as a placeholder
    null,  // Index 62 is missing, so we use null as a placeholder
    null,  // Index 63 is missing, so we use null as a placeholder
    null,  // Index 64 is missing, so we use null as a placeholder
    null,  // Index 65 is missing, so we use null as a placeholder
    null,  // Index 66 is missing, so we use null as a placeholder
    null,  // Index 67 is missing, so we use null as a placeholder
    null,  // Index 68 is missing, so we use null as a placeholder
    null,  // Index 69 is missing, so we use null as a placeholder
    null,  // Index 70 is missing, so we use null as a placeholder
    null,  // Index 71 is missing, so we use null as a placeholder
    null,  // Index 72 is missing, so we use null as a placeholder
    null,  // Index 73 is missing, so we use null as a placeholder
    null,  // Index 74 is missing, so we use null as a placeholder
    null,  // Index 75 is missing, so we use null as a placeholder
    null,  // Index 76 is missing, so we use null as a placeholder
    null,  // Index 77 is missing, so we use null as a placeholder
    null,  // Index 78 is missing, so we use null as a placeholder
    null,  // Index 79 is missing, so we use null as a placeholder
    null,  // Index 80 is missing, so we use null as a placeholder
    null,  // Index 81 is missing, so we use null as a placeholder
    null,  // Index 82 is missing, so we use null as a placeholder
    null,  // Index 83 is missing, so we use null as a placeholder
    null,  // Index 84 is missing, so we use null as a placeholder
    null,  // Index 85 is missing, so we use null as a placeholder
    null,  // Index 86 is missing, so we use null as a placeholder
    "ACN Communications Sweden AB"
)


suspend fun parseCSV(path: Reader, dao : CellTowerDao) {
    val reader = CSVReader(path)
    var nextLine: Array<String>?

    while (reader.readNext().also { nextLine = it } != null) {
        val radio = nextLine!![0]
        val mcc = nextLine!![1].toInt()
        val mnc = providers[nextLine!![2].toInt()]
        val cid = nextLine!![4].toInt()
        val longitude = nextLine!![6].toFloat()
        val latitude = nextLine!![7].toFloat()
        val range = nextLine!![8].toFloat()
        val samples = nextLine!![9].toInt()

        if (samples < MINIMUM_SAMPLES) {
            nextLine = reader.readNext()
        }

        if(radio == "LTE"){
            val celltower = CellTower(
                radio = radio,
                mcc = mcc,
                mnc = mnc,
                cid = cid,
                longitude = longitude,
                latitude = latitude,
                range = range,
            )

            dao.upsertCellTower(celltower)

        }




    }

}

