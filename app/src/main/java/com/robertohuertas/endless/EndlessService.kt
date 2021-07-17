package com.robertohuertas.endless

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.os.SystemClock
import android.provider.Settings

import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.activityViewModels
import com.robertohuertas.endless.repository.NewsRepo
import com.robertohuertas.endless.viewmodel.MyViewmodel
import kotlinx.coroutines.*


class EndlessService : Service() {

  lateinit var notfin : NotificationManagerCompat
   lateinit var not : Notification
    val CHANNEL_ID = "channelID"
    val CHANNEL_NAME = "channelname"
    val NOTIFICATION_ID =0
    var d : String = "221005"
    var isET : Boolean = true
    var isFT : Boolean = true
   // lateinit var  activity : MainActivity

    private var wakeLock: PowerManager.WakeLock? = null
    private var isServiceStarted = false
    var x=1

    override fun onBind(intent: Intent): IBinder? {
        log("Some component want to bind with the service")
        // We don't provide binding, so return null
        return null
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        log("onStartCommand executed with startId: $startId")
        if (intent != null) {
            val action = intent.action
            log("using an intent with action $action")
            when (action) {
                Actions.START.name -> startService()
                Actions.STOP.name -> stopService()
                else -> log("This should never happen. No action in the received intent")
            }
        } else {
            log(
                "with a null intent. It has been probably restarted by the system."
            )
        }
        if(intent!=null)
        {
            intent?.getStringExtra("PINCODE").also { d = it.toString() }
            intent?.getBooleanExtra("IS_EIGHTEEN",true).also {
                if (it != null) {
                    isET = it
                }
            }
            intent?.getBooleanExtra("IS_FIRSTDOSE",true).also {  if (it != null) {
                isFT = it
            } }
        }

        Log.i("IS_","onStartCalled")
        // by returning this we make sure the service is restarted if the system kills the service
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        log("The service has been created".toUpperCase())
        val notification = createNotification()
        startForeground(1, notification)
        //activity as MainActivity

    }

    override fun onDestroy() {
        super.onDestroy()
        log("The service has been destroyed".toUpperCase())
        Toast.makeText(this, "Service destroyed", Toast.LENGTH_SHORT).show()
    }

    override fun onTaskRemoved(rootIntent: Intent) {
        val restartServiceIntent = Intent(applicationContext, EndlessService::class.java).also {
            it.setPackage(packageName)
        };
        val restartServicePendingIntent: PendingIntent = PendingIntent.getService(this, 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        applicationContext.getSystemService(Context.ALARM_SERVICE);
        val alarmService: AlarmManager = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager;
        alarmService.set(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000, restartServicePendingIntent);
    }
    
    private fun startService() {
        if (isServiceStarted) return
        log("Starting the foreground service task")
        Toast.makeText(this, "Service starting its task", Toast.LENGTH_SHORT).show()
        isServiceStarted = true
        setServiceState(this, ServiceState.STARTED)

        // we need this lock so our service gets not affected by Doze Mode
        wakeLock =
            (getSystemService(Context.POWER_SERVICE) as PowerManager).run {
                newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "EndlessService::lock").apply {
                    acquire()
                }
            }
        Log.i("BEFIRE CALLING ","ABOUT TO ITERATE")
        ourNotification()
        // we're starting a loop in a coroutine
        GlobalScope.launch(Dispatchers.IO) {
            while (isServiceStarted) {
                launch(Dispatchers.IO) {
                    new("512","17-07-2021")
                  //  Log.d("SUN FINCTION${x}",com.robertohuertas.endless.atask.taska().d.toString())

                    x=x+1
                    log(x.toString())
                }
                delay(1 * 20 * 1000)
            }
            log("End of the loop for the service")
        }
    }

    private fun stopService() {
        log("Stopping the foreground service")
        Toast.makeText(this, "Service stopping", Toast.LENGTH_SHORT).show()
        try {
            wakeLock?.let {
                if (it.isHeld) {
                    it.release()
                }
            }
            stopForeground(true)
            stopSelf()
        } catch (e: Exception) {
            log("Service stopped without being started: ${e.message}")
        }
        isServiceStarted = false
        setServiceState(this, ServiceState.STOPPED)
    }

        fun new(dis :String , dte : String) {

           val sharedPreferences = getSharedPreferences("MySharedPref", MODE_PRIVATE)
           val newd = sharedPreferences?.getString("s_PIN","221005")
            val neET = sharedPreferences?.getBoolean("s_EE",true)
            val neEF = sharedPreferences?.getBoolean("s_EF",true)

            GlobalScope.launch {
                val api = NewsRepo()
                Log.i("D VALUE IS",d)
              var x=api.getbyPin(newd!!,dte).body()?.sessions
                Log.i("IS_NEWD",newd.toString())
               Log.i("IS_NEWXVALE",x.toString())
                Log.i("IS_E",neET.toString())
                Log.i("IS_F",neEF.toString())
                Log.i("IS_D",d)

//                var flag = sharedPreferences.getBoolean("notify_button",false)
//                Log.i("IS_flginSEeerc",flag.toString())

                if(neET == true)
                {
                    x= x?.filter { it.min_age_limit ==18 }
                }else
                {
                    x= x?.filter { it.min_age_limit ==45 }
                }
                if(neEF == true)
                {
                    x= x?.filter { it.available_capacity_dose1 > 0 }
                }else
                {
                    x= x?.filter { it.available_capacity_dose2 > 0 }
                }
                if(x?.isEmpty() == true)
                {
                    Log.i("IS_FASE",x.toString())
                }else
                {
                    Log.i("IS_TRUE",x.toString())
                    notfin.notify(NOTIFICATION_ID,not)
                }
//                for (i in api.getbyPin(dis,dte).body()!!.sessions)
//                {
//
//                        Log.i("Zero Capacity",i.address)
//
//                        notfin.notify(NOTIFICATION_ID,not)
//
//                }
                Log.i("states", "new: ${api.getStates().body()!!.states}")
                Log.i("district", "new: ${api.getDistricts().body()!!.districts}")
            }
        }

    private fun ourNotification()  {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel =NotificationChannel(CHANNEL_ID,CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT)

            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel (channel)
        }

        val pendingIntent: PendingIntent = Intent(this, MainActivity::class.java).let { notificationIntent ->
            PendingIntent.getActivity(this, 0, notificationIntent, 0)
        }


        not = NotificationCompat.Builder(this,CHANNEL_ID)
            .setContentTitle("Vaccine Avilabel")
            .setContentText("Click Now To Book")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .build()

      notfin = NotificationManagerCompat.from(this)



    }

    private fun createNotification(): Notification {
        val notificationChannelId = "ENDLESS SERVICE CHANNEL"

        // depending on the Android API that we're dealing with we will have
        // to use a specific method to create the notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                notificationChannelId,
                "Endless Service notifications channel",
                NotificationManager.IMPORTANCE_HIGH
            ).let {
                it.description = "Endless Service channel"
                it.enableLights(true)
                it.lightColor = Color.RED
                it.enableVibration(true)
                it.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                it
            }
            notificationManager.createNotificationChannel(channel)
        }

        val pendingIntent: PendingIntent = Intent(this, MainActivity::class.java).let { notificationIntent ->
            PendingIntent.getActivity(this, 0, notificationIntent, 0)
        }

        val builder: Notification.Builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) Notification.Builder(
            this,
            notificationChannelId
        ) else Notification.Builder(this)

        return builder
            .setContentTitle("App Working")
            .setContentText("This is your favorite endless service working")
            .setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setTicker("Ticker text")
            .setPriority(Notification.PRIORITY_HIGH) // for under android 26 compatibility
            .build()
    }

//    data class Covid(
//        val sessions: List<Session>
//    )
//    data class Session(
//        val address: String,
//        val available_capacity: Int,
//        val available_capacity_dose1: Int,
//        val available_capacity_dose2: Int,
//        val block_name: String,
//        val center_id: Int,
//        val date: String,
//        val district_name: String,
//        val fee: String,
//        val fee_type: String,
//        val from: String,
//        val lat: Double,
//        val long: Double,
//        val min_age_limit: Int,
//        val name: String,
//        val pincode: Int,
//        val session_id: String,
//        val slots: List<String>,
//        val state_name: String,
//        val to: String,
//        val vaccine: String
//    )

}
