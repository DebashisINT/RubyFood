package com.rubyfood.features.dashboard.presentation

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import android.text.TextUtils
import com.rubyfood.app.AlarmReceiver
import com.rubyfood.app.Pref
import com.rubyfood.app.utils.AppUtils
import com.rubyfood.features.location.LocationWizard
import com.elvishew.xlog.XLog

class SystemEventReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED" || intent.action == "android.intent.action.AIRPLANE_MODE" ||
                intent.action == "android.intent.action.ACTION_SHUTDOWN") {

            if (intent.action == "android.intent.action.BOOT_COMPLETED")
                XLog.e("=======================Boot Completed successfully ${AppUtils.getCurrentDateTime()} (SystemEventReceiver)=======================")
            else if(intent.action == "android.intent.action.AIRPLANE_MODE") {
                var text = ""

                text = if (Settings.Global.getInt(context.contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) != 0)
                    "Airplane Mode is On "
                else
                    "Airplane Mode is Off "
                XLog.e("========================${text + AppUtils.getCurrentDateTime()}=======================")
            }else if(intent.action == "android.intent.action.ACTION_SHUTDOWN"){
                val locationName = LocationWizard.getLocationName(context, Pref.latitude!!.toDouble(), Pref.longitude!!.toDouble())
                XLog.e("\n======================== \n Phone Shutdown || DateTime : ${AppUtils.getCurrentDateTime()} || Location : last_lat: ${Pref.latitude} || last_long: ${Pref.longitude} || LocationName ${locationName} \n=======================")
            }else if(intent.action == "android.os.action.POWER_SAVE_MODE_CHANGED"){
                XLog.e("\n android.os.action.POWER_SAVE_MODE_CHANGED")
            }

        }
    }
}