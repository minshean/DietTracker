package com.diettracker.application

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import com.diettracker.R
import com.diettracker.core.navigation.NavigationDispatcher
import com.diettracker.core.snackbars.SnackbarDispatcher
import com.diettracker.core.snackbars.SnackbarType
import com.diettracker.databinding.ActivityMainBinding
import com.diettracker.notifications.DailyReceiver
import com.google.android.material.snackbar.Snackbar
import com.zhuinden.liveevent.observe
import org.koin.android.ext.android.inject
import java.util.*

class MainActivity : AppCompatActivity() {

    private val navigationDispatcher by inject<NavigationDispatcher>()
    private val snackbarDispatcher by inject<SnackbarDispatcher>()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
//        )

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navigationDispatcher.navigationCommands.observe(this){ command ->
            Navigation.findNavController(this, R.id.navHostFragment).command(this)
        }

        setAlarms()

        setUpSnackbarObserver()
    }

    private fun setAlarms() {
        val calendar1 = Calendar.getInstance()
        val calendar2 = Calendar.getInstance()
        val calendar3 = Calendar.getInstance()

        calendar1[Calendar.HOUR_OF_DAY] = 8
        calendar1[Calendar.MINUTE] = 30
        calendar1[Calendar.SECOND] = 0
        calendar1[Calendar.MILLISECOND] = 0
        calendar2[Calendar.HOUR_OF_DAY] = 13
        calendar2[Calendar.MINUTE] = 30
        calendar2[Calendar.SECOND] = 0
        calendar2[Calendar.MILLISECOND] = 0
        calendar3[Calendar.HOUR_OF_DAY] = 0
        calendar3[Calendar.MINUTE] = 42
        calendar3[Calendar.SECOND] = 0
        calendar3[Calendar.MILLISECOND] = 0


        val cur = Calendar.getInstance()

        if (cur.after(calendar1)) {
            calendar1.add(Calendar.DATE, 1)
        }

        if (cur.after(calendar2)) {
            calendar2.add(Calendar.DATE, 1)
        }

        if (cur.after(calendar3)) {
            calendar3.add(Calendar.DATE, 1)
        }

        val myIntent = Intent(applicationContext, DailyReceiver::class.java)
        val ALARM1_ID = 10000
        val ALARM2_ID = 20000
        val ALARM3_ID = 30000
        val pendingIntent1 = PendingIntent.getBroadcast(
            applicationContext,
            ALARM1_ID,
            myIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val pendingIntent2 = PendingIntent.getBroadcast(
            applicationContext,
            ALARM2_ID,
            myIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val pendingIntent3 = PendingIntent.getBroadcast(
            applicationContext,
            ALARM3_ID,
            myIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
            calendar1.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent1)
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
            calendar2.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent2)
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
            calendar3.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent3)


    }

    @Suppress("DEPRECATION")
    private fun setUpSnackbarObserver() {
        snackbarDispatcher.snackbars.observe(this){ snack ->
            val snackbar = Snackbar.make(
                binding.root,
                snack.msg,
                if(snack.isLong) Snackbar.LENGTH_LONG else Snackbar.LENGTH_SHORT
            )

            if(snack.snackbarType is SnackbarType.Error){
                snackbar.setBackgroundTint(resources.getColor(android.R.color.holo_red_dark))
            }

            if(snack.snackbarType is SnackbarType.Success){
                snackbar.setBackgroundTint(resources.getColor(android.R.color.holo_green_dark))
            }

            snackbar.show()
        }
    }
}