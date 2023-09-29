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
import com.hkr.mockupforproject.data.CellTower
import com.hkr.mockupforproject.data.CellTowerDao
import com.hkr.mockupforproject.data.MINIMUM_SAMPLES
import com.hkr.mockupforproject.data.providers
import com.hkr.mockupforproject.ui.AppViewModel
import com.hkr.mockupforproject.ui.theme.MockupForProjectTheme
import com.opencsv.CSVReader
import java.io.FileReader
import com.hkr.mockupforproject.data.parseCSV

class MainActivity : ComponentActivity() {

    val viewmodel : AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewmodel.init_db(applicationContext)
        val db = viewmodel.db


        val cellTowerDao = db.cellTowerDao()

        parseCSV(cellTowerDao)

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


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MockupForProjectTheme {
        //StartScreen()
    }
}