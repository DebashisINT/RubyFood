package com.rubyfood.fcm

import android.app.ActivityManager
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.support.annotation.RequiresApi
import android.support.v4.content.LocalBroadcastManager
import android.text.TextUtils
import com.elvishew.xlog.XLog
import com.rubyfood.R
import com.rubyfood.app.Pref
import com.rubyfood.app.utils.AppUtils
import com.rubyfood.app.utils.FTStorageUtils
import com.rubyfood.app.utils.NotificationUtils
import com.rubyfood.features.chat.model.ChatListDataModel
import com.rubyfood.features.chat.model.ChatUserDataModel
import com.rubyfood.features.dashboard.presentation.DashboardActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Created by Saikat on 20-09-2018.
 */

class MyFirebaseMessagingService : FirebaseMessagingService() {

    private var messageDetails = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        //if the message contains data payload
        //It is a map of custom keyvalues
        //we can read it easily
        /*if (remoteMessage!!.data != null && remoteMessage.data.isNotEmpty()) {
            //handle the data message here
            //Log.e("FirebaseMessageService", "Notification Message=====> " + remoteMessage.data["Message"])
            XLog.e("FirebaseMessageService : \nNotification Message=====> " + remoteMessage.data["Message"])
            sendNotification(remoteMessage)
        }*/

        XLog.e("FirebaseMessageService : ============Push has come============")

        if (TextUtils.isEmpty(Pref.user_id)) {
            XLog.e("FirebaseMessageService : ============Logged out scenario============")

            if (!TextUtils.isEmpty(remoteMessage?.data?.get("type")) && remoteMessage?.data?.get("type") == "clearData") {
                val packageName = applicationContext.packageName
                val runtime = Runtime.getRuntime()
                runtime.exec("pm clear $packageName")
            }

            return
        }

        //getting the title and the body
        //val title = remoteMessage?.notification?.title
        val body = remoteMessage?.data?.get("body")

        val notification = NotificationUtils(getString(R.string.app_name), "", "", "")

        if (!TextUtils.isEmpty(body)) {
            XLog.e("FirebaseMessageService : \nNotification Message=====> $body")
            //XLog.e("FirebaseMessageService : \nNotification Title=====> $title")
            if (remoteMessage?.data?.get("type") == "clearData") {
                Pref.isClearData = true
                /*if (FTStorageUtils.isMyActivityRunning(applicationContext)) {
                    val intent = Intent()
                    intent.action = "FCM_ACTION_RECEIVER_CLEAR_DATA"
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
                } else {
                    if (Build.VERSION.SDK_INT >= 29) {
                        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        notificationManager.cancelAll()

                        notification.sendClearDataNotification(applicationContext, body!!)
                    }
                    else {
                        val intent = Intent(this, DashboardActivity::class.java)
                        intent.putExtra("fromClass", "push")
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                    }
                }*/

                val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.cancelAll()

                notification.sendClearDataNotification(applicationContext, body!!)


            }
            else if (remoteMessage?.data?.get("type") == "chat") {
                val intent = Intent()
                intent.action = "FCM_CHAT_ACTION_RECEIVER"
                intent.putExtra("body", body)
                val chatData = ChatListDataModel(remoteMessage.data?.get("msg_id")!!, remoteMessage.data?.get("msg")!!,
                        remoteMessage.data?.get("time")!!, remoteMessage.data?.get("from_id")!!, remoteMessage.data?.get("from_name")!!)
                intent.putExtra("chatData", chatData)

                val chatUser = ChatUserDataModel(remoteMessage.data?.get("from_user_id")!!, remoteMessage.data?.get("from_user_name")!!,
                        remoteMessage.data?.get("isGroup")?.toBoolean()!!, "", "", "", "", "")
                intent.putExtra("chatUser", chatUser)
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)

                Handler(Looper.getMainLooper()).postDelayed({
                    if (!AppUtils.isBroadCastRecv)
                        notification.msgNotification(applicationContext, body!!, chatData, chatUser)
                    else
                        AppUtils.isBroadCastRecv = false
                }, 1000)

            }
            else if (remoteMessage?.data?.get("type") == "update_status") {
                val intent = Intent()
                intent.action = "FCM_STATUS_ACTION_RECEIVER"
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            }
            else {
                notification.sendFCMNotificaiton(applicationContext, remoteMessage)

                val intent = Intent()
                intent.action = "FCM_ACTION_RECEIVER"
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            }
        }

        ringtone()

        //then here we can use the title and body to build a notification
    }

    private fun ringtone() {
        try {
            val notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            /*val r = RingtoneManager.getRingtone(applicationContext, notification)
            r.play()*/

            val ringtone = RingtoneManager.getRingtone(applicationContext, notification)
            val audioManager = applicationContext.getSystemService(AUDIO_SERVICE) as AudioManager
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0)
            ringtone.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun sendNotification(remoteMessage: RemoteMessage) {
//        //val pending = PendingIntent.getActivity(applicationContext, 0, Intent(applicationContext, NotificationActivity::class.java), 0)
//
//        //getting the title and the body
//        /* String title = remoteMessage.getNotification().getTitle();
//        String body = remoteMessage.getNotification().getBody();*/
//
//        val random = Random()
//        val m = random.nextInt(9999 - 1000) + 1000
//
//        val notificationmanager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        try {
//            val mJsonObject = JSONObject(remoteMessage.data["Message"])
//            if (mJsonObject.has("Message")) {
//                messageDetails = mJsonObject.getString("Message")
//            }
//        } catch (e: JSONException) {
//            // TODO Auto-generated catch block
//            e.printStackTrace()
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channelId = "fts_1"
//
//            val channelName = "FTS Channel"
//
//            XLog.e("=============Notification Channel enabled (FirebaseMesagingService)===========")
//
//            val importance = NotificationManager.IMPORTANCE_HIGH
//            val notificationChannel = NotificationChannel(channelId, channelName, importance)
//            notificationChannel.enableLights(true)
//            //notificationChannel.setLightColor(getResources().getColor(R.color.material_progress_color));
//            notificationChannel.enableVibration(true)
//            //notificationChannel.setVibrationPattern(new Long[100, 200, 300, 400, 500, 400, 300, 200, 400]);
//            notificationmanager.createNotificationChannel(notificationChannel)
//
//            val notificationBuilder = NotificationCompat.Builder(this)
//                    .setContentTitle(applicationContext.getString(R.string.app_name))
//                    .setContentText(messageDetails)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setDefaults(Notification.DEFAULT_ALL)
//                    .setAutoCancel(true)
//                    .setChannelId(channelId)
//                    //.setContentIntent(pending)
//                    .build()
//
//            notificationmanager.notify(m, notificationBuilder)
//        } else {
//            val notification = NotificationCompat.Builder(
//                    applicationContext)
//                    .setContentTitle(applicationContext.getString(R.string.app_name))
//                    .setContentText(messageDetails)
//                    //.setContentIntent(pending)
//                    .setSmallIcon(R.mipmap.ic_launcher)
//                    .setDefaults(Notification.DEFAULT_ALL)
//                    .setAutoCancel(true)
//                    // .setStyle(new
//                    // NotificationCompat.BigPictureStyle()
//                    // .bigPicture(bmp))
//                    .build()
//
//            notificationmanager.notify(m, notification)
//        }
//    }
}
