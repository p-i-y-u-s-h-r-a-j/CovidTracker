package com.example.covid_19tracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.Duration.parse
import java.util.*
import java.util.concurrent.TimeUnit
import java.util.logging.Level.parse

class MainActivity : AppCompatActivity() {

    lateinit var stateAdapter: StateAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        List.addHeaderView(LayoutInflater.from(this).inflate(R.layout.item_title,List,false))
        fetchresults()
    }

    private fun fetchresults() {
        GlobalScope.launch {
            val response = withContext(Dispatchers.IO) { Client.api.execute() }
            if(response.isSuccessful){
                val data = Gson().fromJson(response.body?.string(),Response::class.java)
                launch(Dispatchers.Main){
                    bindCombineData(data.statewise[0])
                    bindStateWiseData(data.statewise.subList(0,data.statewise.size))
                }

            }
        }
    }

    private fun bindStateWiseData(subList: List<StatewiseItem>) {
        stateAdapter = StateAdapter(subList)
        List.adapter = stateAdapter
    }

    private fun bindCombineData(data: StatewiseItem) {
        val lastUpdatedTime = data.lastupdatedtime
        val simpledateFormat = SimpleDateFormat("dd/MM/yyyy HH:MM:SS")
        lastUpdatedTv.text = "Last Updated \n ${GetTimeAgo(simpledateFormat.parse(lastUpdatedTime))}"

        confirmedTv.text = data.confirmed
        DeathTv.text = data.deaths
        ActiveTv.text = data.active
        RecoveredTv.text = data.recovered
    }
    fun GetTimeAgo(past: Date): String{
        val now = Date()
        val second:Long = TimeUnit.MILLISECONDS.toSeconds(now.time-past.time)
        val minute:Long = TimeUnit.MILLISECONDS.toMinutes(now.time-past.time)
        val hours:Long = TimeUnit.MILLISECONDS.toHours(now.time-past.time)

        return when {
            second < 60 -> {
                "Few Seconds Ago"
            }
            minute <60 -> {
                "$minute min Ago"
            }
            hours < 24 -> {
                "$hours Hours Ago"
            }
            else ->{
                SimpleDateFormat("dd/MM/yy, hh:mm a").format(past).toString()
            }

        }

    }
}