package com.facultate.myapplication.firebase

import android.app.*
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.os.bundleOf
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.Navigation
import com.facultate.myapplication.MainActivity
import com.facultate.myapplication.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = "FCM-SERVICE"

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

//        remoteMessage.notification?.let { notification->
//            val pendingIntent = NavDeepLinkBuilder(applicationContext)
//                .setComponentName(MainActivity::class.java)
//                .setGraph(R.navigation.nav_graph_master)
//                .setDestination(R.id.home_fragment)
//                .createPendingIntent()
//
//            val notificationBuilder = NotificationCompat.Builder(applicationContext, getString(R.string.default_notification_channel_id))
//                .setContentTitle(notification.title)
//                .setContentText(notification.body)
//                .setSmallIcon(R.drawable.cart)
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true)
//
//            val notificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.notify(0, notificationBuilder.build())
//        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "New Token: $token")
    }


}
