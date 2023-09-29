package com.hkr.mockupforproject

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.hkr.mockupforproject.data.CellTower
import com.hkr.mockupforproject.data.parseCSV
import com.hkr.mockupforproject.ui.AppViewModel
import com.hkr.mockupforproject.ui.theme.MockupForProjectTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.Reader

class MainActivity : ComponentActivity() {

    val viewmodel : AppViewModel by viewModels()
    var towers : List<CellTower> = emptyList()

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)





        setContent {
            MockupForProjectTheme {

                LaunchedEffect(Unit) {
                    withContext(Dispatchers.IO) {
                        // Run your function in a separate thread here
                        test()
                    }

                    withContext(Dispatchers.Main){
                        Log.d("MAIN", "Hi")
                        Toast.makeText(applicationContext, towers.size.toString(), Toast.LENGTH_LONG).show()
                    }
                }


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


    suspend fun test(){
        viewmodel.init_db(applicationContext)
        val db = viewmodel.db
        val cellTowerDao = db.cellTowerDao()


        val reader : Reader = resources.openRawResource(R.raw.data).reader()

        val re = parseCSV(reader,cellTowerDao)

        towers = cellTowerDao.getAll()

        Log.d("MAINACTIVITY", towers.size.toString())
        Toast.makeText(applicationContext, towers.size.toString(), Toast.LENGTH_LONG).show()
    }
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MockupForProjectTheme {
        //StartScreen()
    }
}