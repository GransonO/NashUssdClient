package com.epitome.nashussdclient

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.epitome.nashussd.UssdRunner
import com.epitome.nashussd.data.Ussd
import com.epitome.nashussd.utils.AccessibilityHelper
import com.epitome.nashussd.utils.AccessibilityHelper.ussdExecutor


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ussdExecutor = UssdRunner(this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun runUssd(view: View){

        ussdExecutor?.run(
            Ussd(
            "1",
            "*544#"
            )
        )

    }

    fun isAccessibilityServiceEnabled(view: View){
        val enabled = AccessibilityHelper.isAccessibilityServiceEnabled(this@MainActivity)
        if(!enabled){
            AccessibilityHelper.openAccessibilitySettings(this@MainActivity)
        }else{
            Toast.makeText(this, "Enabled", Toast.LENGTH_LONG).show()
        }
    }

    fun isPermissionGranted(view: View){
        AccessibilityHelper.isPermissionGranted(this@MainActivity)
    }

    fun requestPermissions(view: View){
        AccessibilityHelper.requestPermissions(this@MainActivity)
    }
}