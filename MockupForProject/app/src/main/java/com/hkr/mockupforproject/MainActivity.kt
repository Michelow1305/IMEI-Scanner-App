package com.hkr.mockupforproject

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hkr.mockupforproject.data.AppDatabase
import com.hkr.mockupforproject.data.AppRepository
import com.hkr.mockupforproject.ui.AppViewModel
import com.hkr.mockupforproject.ui.AppViewModelFactory
import com.hkr.mockupforproject.ui.theme.MockupForProjectTheme

class MainActivity : ComponentActivity() {

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val database by lazy { AppDatabase.getDatabase(this) }
        val repository by lazy { AppRepository(database.cellTowerDao()) }
        val viewModelFactory = AppViewModelFactory(repository)

        val viewModel : AppViewModel by viewModels {
            viewModelFactory
        }

        viewModel.getCellTowersInRange(56.049877F, 14.150383F)
        viewModel.findByCid(208942101)
        //viewModel.findByMnc("Telia")

        setContent {
            //val cellTowers by viewModel.findByMncResult.observeAsState(initial = emptyList())
            val cellTowers by viewModel.cellTowersInRangeResult.observeAsState(initial = emptyList())

            MockupForProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //AppScreen()

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White, shape = RoundedCornerShape(10.dp))
                            .height(200.dp)

                    ) {
                        items(cellTowers) { step ->
                            Log.d("", viewModel.findByCidResult.collectAsState().toString())
                            //Log.d("",step.toString())
                            Text(

                                text = step.toString()

                            )

                        }

                    }
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