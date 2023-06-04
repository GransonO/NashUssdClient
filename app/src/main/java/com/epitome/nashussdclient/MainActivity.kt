package com.epitome.nashussdclient

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.epitome.nashussd.data.USSDPayload
import com.epitome.nashussd.utils.AccessibilityHelper.initNashClient
import com.epitome.nashussd.utils.AccessibilityHelper.requestAccessibilityServiceUtil
import com.epitome.nashussd.utils.AccessibilityHelper.requestPhonePermissionsUtil
import com.epitome.nashussd.utils.AccessibilityHelper.runUssdUtil


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initNashClient(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun runUssd(view: View){
        runUssdUtil(
            USSDPayload(
                id = "1",
                code = "*544#",
                promptFlow = "2*6*1*1",
                hasPin = false
            )
        )
    }
    fun requestPhonePermissions(view: View){
        requestPhonePermissionsUtil(this@MainActivity)
    }

    fun requestAccessibilityService(view: View){
        requestAccessibilityServiceUtil(this@MainActivity)
    }
}