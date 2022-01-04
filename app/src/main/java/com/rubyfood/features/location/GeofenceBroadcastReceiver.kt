package com.rubyfood.features.location

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent



class GeofenceBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        GeofenceTransitionsJobIntentService.enqueueWork(context, intent)

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            GeofenceTransitionsJobIntentService.enqueueWork(context, intent)
        } else {
            val intent_ = Intent(context, GeofenceTransitionsIntentService::class.java)
            context.startService(intent_)
        }*/
    }
}
