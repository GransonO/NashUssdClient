package com.epitome.nashussd.utils

import android.Manifest
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.accessibility.AccessibilityManager
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.epitome.nashussd.services.UssdRunner
import com.epitome.nashussd.data.USSDPayload
import com.epitome.nashussd.interfaces.USSDExecutor
import com.epitome.nashussd.interfaces.UssdCallback
import com.epitome.nashussd.services.SimCardUtil
import com.epitome.nashussd.services.UssdService.Companion.ussdPromptFlow
import com.epitome.nashussd.ui.PermissionsActivity
import com.epitome.nashussd.utils.enums.PermissionEnums


object AccessibilityHelper {

    private const val CALL_USSD_PERMISSION = 275
    private const val PHONE_STATE_PERMISSION = 725
    var ussdExecutor: USSDExecutor? = null
    var nashRequestPlaced: Boolean = false

    fun initNashClient(activity: Activity){
        // isAppForeground()
        ussdExecutor = UssdRunner(activity.applicationContext)
    }

    fun isAppForeground() : Boolean {
        val appProcessInfo =  ActivityManager.RunningAppProcessInfo()
        Log.d("Testing Process", "------------------> ${appProcessInfo.processName}")
        Log.d("Testing Process", "------------------> ${appProcessInfo.describeContents()}")

        ActivityManager.getMyMemoryState(appProcessInfo);
        return (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND || appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE)
    }

    fun isPhonePermissionGranted(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
    }
    fun isPhoneStatePermissionGranted(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.READ_PHONE_STATE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isAccessibilityServiceEnabled(context: Context): Boolean {
        var accessibilityServiceEnabled = false
        val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val runningServices = am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC)
        for (service in runningServices) {
            if (service.resolveInfo.serviceInfo.packageName == context.packageName) {
                accessibilityServiceEnabled = true
            }
        }
        return accessibilityServiceEnabled
    }

    fun requestPhonePermissions(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.CALL_PHONE),
            CALL_USSD_PERMISSION
        )
    }
    fun requestPhoneStatePermissions(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.READ_PHONE_STATE),
            PHONE_STATE_PERMISSION
        )
    }

    fun openAccessibilitySettings(context: Context) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun runUssdUtil(ussd: USSDPayload, ussdCallback: UssdCallback){
        ussdPromptFlow = ussd.promptFlow.split("*")
        ussdExecutor?.execute(ussd, ussdCallback)

    }
    fun requestPhonePermissionsUtil(context: Context){
        val intent = Intent(context, PermissionsActivity::class.java)
        intent.putExtra("permission", PermissionEnums.PHONE.toString())
        context.startActivity(intent)
    }
    fun requestPhoneStatePermissionsUtil(context: Context){
        val intent = Intent(context, PermissionsActivity::class.java)
        intent.putExtra("permission", PermissionEnums.STATE.toString())
        context.startActivity(intent)
    }

    fun requestAccessibilityServiceUtil(context: Context){
        val intent = Intent(context, PermissionsActivity::class.java)
        intent.putExtra("permission", PermissionEnums.SERVICE.toString())
        context.startActivity(intent)
    }

    fun nashSimProviders(context: Context): List<String> = SimCardUtil().getSimCardCouriers(context);

}