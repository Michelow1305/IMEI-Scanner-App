package com.hkr.mockupforproject.data

import com.opencsv.CSVReader
import java.io.FileReader
fun main() {
    val csvFilePath = "240.csv"

    try {
        CSVReader(FileReader(csvFilePath)).use { csvReader ->
            var record: Array<String>?
            while (csvReader.readNext().also { record = it } != null) {
                // Process each record
                record?.forEach { field ->
                    println(field)
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
data class ParseOpenCelliD(val column1: String) {
}