package com.epitome.nashussd.services

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.SubscriptionInfo
import android.telephony.SubscriptionManager
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService


class SimCardUtil {

    private fun isPhoneStatePermissionGranted(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun getSimCardCouriers(context: Context): List<String>{
        return if(isPhoneStatePermissionGranted(context)){
            simTest(context)
        }else{
            Toast.makeText(context, "Please allow all permissions", Toast.LENGTH_LONG).show()
            listOf()
        }
    }

    fun simTest(context: Context): List<String>{

        //above 22
        if (Build.VERSION.SDK_INT > 22) {
            //for dual sim mobile
            val localSubscriptionManager = context.getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE) as SubscriptionManager
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // Permission required first
                return listOf()
            }

            return if (localSubscriptionManager.activeSubscriptionInfoCount > 1) {
                //if there are two sims in dual sim mobile
                val localList: List<*> = localSubscriptionManager.activeSubscriptionInfoList
                val simInfo = localList[0] as SubscriptionInfo
                val simInfo1 = localList[1] as SubscriptionInfo
                val sim1 = simInfo.displayName.toString()
                val sim2 = simInfo1.displayName.toString()
                listOf(sim1, sim2)
            } else {
                //if there is 1 sim in dual sim mobile
                val tManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                val sim1 = tManager.networkOperatorName
                listOf(sim1)
            }

        } else {
            //below android version 22
            val tManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val sim1 = tManager.networkOperatorName
            return listOf(sim1)
        }
    }
}