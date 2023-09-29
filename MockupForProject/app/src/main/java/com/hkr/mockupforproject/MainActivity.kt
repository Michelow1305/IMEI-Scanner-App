package com.hkr.mockupforproject

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import com.hkr.mockupforproject.data.CellTower
import com.hkr.mockupforproject.data.parseCSV
import com.hkr.mockupforproject.ui.AppViewModel
import com.hkr.mockupforproject.ui.theme.MockupForProjectTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.Reader
import java.util.concurrent.Executors


class MainActivity : ComponentActivity() {

    val viewmodel : AppViewModel by viewModels()
    var towers : List<CellTower> = emptyList()

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            MockupForProjectTheme {
                val coroutineScope = rememberCoroutineScope()

                LaunchedEffect(Unit) {
                    coroutineScope.launch(Dispatchers.Default) {
                        // Your function code here
                        test()
                        // When the function finishes running, send a Toast
                        //showToast(towers.size.toString())
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

        parseCSV(reader,cellTowerDao)

        towers = cellTowerDao.getAll()


        towers.forEach { i ->
            showToast("",to = i)
        }

    }

    fun showToast(message: String, to : CellTower) {
        Handler(Looper.getMainLooper()).post {
            // Display the toast here
           // Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
            Log.d("MainActivity", to.toString())
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