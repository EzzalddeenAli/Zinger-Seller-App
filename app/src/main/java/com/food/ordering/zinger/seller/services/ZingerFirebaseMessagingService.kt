package com.food.ordering.zinger.seller.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.food.ordering.zinger.seller.R
import com.food.ordering.zinger.seller.data.local.PreferencesHelper
import com.food.ordering.zinger.seller.data.model.OrderNotificationPayload
import com.food.ordering.zinger.seller.data.model.ShopConfigurationModel
import com.food.ordering.zinger.seller.ui.home.HomeActivity
import com.food.ordering.zinger.seller.ui.webview.WebViewActivity
import com.food.ordering.zinger.seller.utils.AppConstants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import org.koin.android.ext.android.inject

class ZingerFirebaseMessagingService : FirebaseMessagingService() {


    private val preferencesHelper: PreferencesHelper by inject()

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d("FCM", "From: ${remoteMessage?.from}")
        Log.d("FCM", "Content: ${remoteMessage?.data}")
        createNotificationChannel()


        remoteMessage.data.let {

            when (it["type"]) {

                AppConstants.NOTIFICATIONTYPE.URL.name -> {
                    val intent = Intent(this, WebViewActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    val title = it["title"]
                    val message = it["message"]
                    val payload = JSONObject(it.get("payload"))
                    if (payload.has("url")) {
                        intent.putExtra(
                            AppConstants.NOTIFICATIONTYPE.URL.name,
                            payload.getString("url").toString()
                        )
                        val pendingIntent: PendingIntent =
                            PendingIntent.getActivity(this, 0, intent, 0)
                        sendNotificationWithPendingIntent(title, message, pendingIntent)
                    }
                }


                AppConstants.NOTIFICATIONTYPE.NEW_ORDER.name -> {

                    var title = it["title"]
                    var message = it["message"]
                    val payload = Gson().fromJson(it["payload"],OrderNotificationPayload::class.java)
                    println(payload)

                }

                AppConstants.NOTIFICATIONTYPE.ORDER_CANCELLED.name -> {

                    var title = it["title"]
                    var message = it["message"]
                    val payload = Gson().fromJson(it["payload"],OrderNotificationPayload::class.java)
                    println(payload)

                }

                AppConstants.NOTIFICATIONTYPE.NEW_ARRIVAL.name -> {
                    //TODO navigate to specific shop
                    val intent = Intent(this, HomeActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    }
                    var title = it["title"]
                    var message = it["message"]
                    val payload = JSONObject(it["payload"])
                    var shopName = ""
                    var shopId = ""
                    if(payload.has("shopName")){
                        shopName = payload.getString("shopName").toString()
                    }
                    if(payload.has("shopId")){
                        shopId = payload.getString("shopId").toString()
                    }
                    if(title.isNullOrEmpty()){
                        title+="New Outlet in you place!"
                    }
                    if(message.isNullOrEmpty()){
                        message+= shopName+" has arrived in your place. Try it out!"
                    }
                    intent.putExtra(AppConstants.SHOP_ID,shopId)
                    val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, 0)
                    sendNotificationWithPendingIntent(title,message,pendingIntent)
                }


            }

        }.run {
            remoteMessage.notification?.let {
                sendNotification(it.title, it.body)
            }
        }
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Orders"
            val descriptionText = "Alerts about order status"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("7698", name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun sendNotification(title: String?, message: String?) {
        val builder = NotificationCompat.Builder(applicationContext, "7698")
            .setSmallIcon(R.drawable.ic_zinger_notification_icon)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(applicationContext)) {
            // TODO change id
            notify(123, builder.build())
        }
    }

    private fun sendNotificationWithPendingIntent(
        title: String?,
        message: String?,
        pendingIntent: PendingIntent
    ) {
        val builder = NotificationCompat.Builder(this, "7698")
            .setSmallIcon(R.drawable.ic_zinger_notification_icon)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message)
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(applicationContext)) {
            // TODO change id
            notify(123, builder.build())
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM", "FCM Token: " + token)
        preferencesHelper.fcmToken = token
        preferencesHelper.isFCMTokenUpdated = false

    }
}