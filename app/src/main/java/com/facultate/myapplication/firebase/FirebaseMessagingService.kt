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
//        Sending notifications trough Postman with a data payload
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "New Token: $token")
    }


}
