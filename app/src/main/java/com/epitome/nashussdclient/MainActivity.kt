package com.epitome.nashussdclient

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import com.epitome.nashussd.data.USSDPayload
import com.epitome.nashussd.interfaces.UssdCallback
import com.epitome.nashussd.utils.AccessibilityHelper.initNashClient
import com.epitome.nashussd.utils.AccessibilityHelper.requestAccessibilityServiceUtil
import com.epitome.nashussd.utils.AccessibilityHelper.requestPhonePermissionsUtil
import com.epitome.nashussd.utils.AccessibilityHelper.runUssdUtil
import com.epitome.nashussdclient.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
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
            ),
            object : UssdCallback{
                override fun onSuccess(value: String) {
                    binding.ussdtext.text = value
                }

                override fun onFailure(value: String) {
                    binding.ussdtext.text = value
                }
            }
        )
    }
    fun requestPhonePermissions(view: View){
        requestPhonePermissionsUtil(this@MainActivity)
    }

    fun requestAccessibilityService(view: View){
        requestAccessibilityServiceUtil(this@MainActivity)
    }
}