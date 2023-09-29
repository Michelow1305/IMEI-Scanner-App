package com.hkr.mockupforproject.ui

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.room.Room
import com.hkr.mockupforproject.data.AppDatabase

class AppViewModel: ViewModel() {
    var searchInfo by mutableStateOf(false)
    lateinit var db : AppDatabase


    fun init_db(context : Context){
        db = Room.databaseBuilder(context, AppDatabase::class.java, "app_database").build()
    }




}