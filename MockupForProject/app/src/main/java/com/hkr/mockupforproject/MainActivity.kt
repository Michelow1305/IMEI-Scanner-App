package com.hkr.mockupforproject

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.room.Room
import com.hkr.mockupforproject.data.AppDatabase
import com.hkr.mockupforproject.data.CellTower
import com.hkr.mockupforproject.data.CellTowerDao
import com.hkr.mockupforproject.data.MINIMUM_SAMPLES
import com.hkr.mockupforproject.data.providers
import com.hkr.mockupforproject.ui.AppViewModel
import com.hkr.mockupforproject.ui.theme.MockupForProjectTheme
import com.opencsv.CSVReader
import java.io.FileReader

class MainActivity : ComponentActivity() {

    val viewmodel : AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewmodel.init_db(applicationContext)
        val db = viewmodel.db


        val cellTowerDao = db.cellTowerDao()

        parseCSV("", cellTowerDao)

        val towers = cellTowerDao.getAll()
        towers.forEach{ i ->
            Log.d("ME", i.toString())
        }

        setContent {
            MockupForProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppScreen()


                //
                //StartScreen(onNextButtonClicked = {})
                //AppScreen()
                }
            }
        }
    }
}

fun parseCSV(path: String, db : CellTowerDao): List<CellTower> {
    val reader = CSVReader(FileReader("app\\src\\main\\java\\com\\hkr\\mockupforproject\\data\\240.csv"))
    val csvObjects = mutableListOf<CellTower>()
    var nextLine: Array<String>?

    while (reader.readNext().also { nextLine = it } != null) {
        val radio = nextLine!![0]
        val mcc = nextLine!![1].toInt()
        val mnc = providers[nextLine!![2].toInt()]
        val cid = nextLine!![4].toInt()
        val longitude = nextLine!![5].toFloat()
        val latitude = nextLine!![6].toFloat()
        val range = nextLine!![7].toFloat()
        val samples = nextLine?.get(8)?.toInt()

        if (samples != null && samples < MINIMUM_SAMPLES && radio != "LTE") {
            nextLine = reader.readNext()
        }
        val celltower = CellTower(
            radio = radio,
            mcc = mcc,
            mnc = mnc,
            cid = cid,
            longitude = longitude,
            latitude = latitude,
            range = range,
        )

        db.insertCellTower(cellTower = celltower)
    }

    return csvObjects
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MockupForProjectTheme {
        //StartScreen()
    }
}