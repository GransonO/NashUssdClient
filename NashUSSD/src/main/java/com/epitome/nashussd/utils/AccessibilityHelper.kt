package com.epitome.nashussd.utils

import android.Manifest
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import android.view.accessibility.AccessibilityManager
import androidx.core.app.ActivityCompat
import com.tester.ussdclient.data.UssdExecutor


object AccessibilityHelper {

    private const val CALL_USSD_PERMISSION = 1
    var ussdExecutor: UssdExecutor? = null

    fun isPermissionGranted(context: Context): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.CALL_PHONE
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun isAccessibilityServiceEnabled(context: Context): Boolean {
        var accessibilityServiceEnabled = false
        val am = context.getSystemService(Context.ACCESSIBILITY_SERVICE) as AccessibilityManager
        val runningServices =
            am.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC)
        for (service in runningServices) {
            if (service.resolveInfo.serviceInfo.packageName == context.getPackageName()) {
                accessibilityServiceEnabled = true
            }
        }
        return accessibilityServiceEnabled
    }

    fun requestPermissions(activity: Activity?) {
        ActivityCompat.requestPermissions(
            activity!!,
            arrayOf<String>(Manifest.permission.CALL_PHONE),
            CALL_USSD_PERMISSION
        )
    }

    fun openAccessibilitySettings(context: Context) {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        context.startActivity(intent)
    }
}