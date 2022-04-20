package com.diettracker.application

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import com.diettracker.R
import com.diettracker.application.di.*
import com.diettracker.notifications.DailyReceiver
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import java.util.*

class App: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(
                appModule,
                firebaseModule,
                servicesModule,
                viewModelsModule,
                databaseModule,
                retrofitModules,
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelOne = NotificationChannel(
                getString(R.string.default_notification_channel_id),
                "Channel One",
                NotificationManager.IMPORTANCE_HIGH
            )
            channelOne.description = "This is channel one for notifications"

            val manager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channels: MutableList<NotificationChannel> = ArrayList()
            channels.add(channelOne)
            manager.createNotificationChannels(channels)
        }
    }
}