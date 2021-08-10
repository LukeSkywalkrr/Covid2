package com.robertohuertas.endless

import android.R.attr.name
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.robertohuertas.endless.databinding.ActivityMainBinding
import com.robertohuertas.endless.viewmodel.MyViewmodel


class MainActivity : AppCompatActivity() {

    val binding : ActivityMainBinding ? = null
    private val model: MyViewmodel by viewModels()
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Covid Vaccine Alert"

        sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
       // val myEdit = sharedPreferences.edit()

        //myEdit.putInt("age", age.getText().toString().toInt())
       // myEdit.commit()
//        findViewById<Button>(R.id.btnStartService).let {
//            it.setOnClickListener {
//                log("START THE FOREGROUND SERVICE ON DEMAND")
//                actionOnService(Actions.START)
//            }
//        }
//
//        findViewById<Button>(R.id.btnStopService).let {
//            it.setOnClickListener {
//                log("STOP THE FOREGROUND SERVICE ON DEMAND")
//                actionOnService(Actions.STOP)
//            }
//        }
        //model.isFirstDose



    }

    fun actionOnService(action: Actions) {
        if (getServiceState(this) == ServiceState.STOPPED && action == Actions.STOP) return
        Intent(this, EndlessService::class.java).also {
            it.action = action.name
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                log("Starting the service in >=26 Mode")
                startForegroundService(it)
                return
            }
            log("Starting the service in < 26 Mode")
            startService(it)
        }
    }
    fun myactionOnService(action: Actions) {
        if (getServiceState(this) == ServiceState.STOPPED && action == Actions.STOP) return

        //

        sharedPreferences.edit().putString("s_PIN", model.demopin).apply()
        sharedPreferences.edit().putBoolean("s_EF", model.isFirstDose).apply()
        sharedPreferences.edit().putBoolean("s_EE", model.iseighteen).apply()


        val intent = Intent(this, EndlessService::class.java)
        intent.putExtra("PINCODE", model.demopin)
        intent.putExtra("IS_EIGHTEEN", model.iseighteen)
        intent.putExtra("IS_FIRSTDOSE", model.isFirstDose)


        intent.also {
            it.action = action.name
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                log("Starting the service in >=26 Mode")
                startForegroundService(it)
                return
            }
            log("Starting the service in < 26 Mode")
            startService(it)
        }
    }
  // var  dis = "512"
}
