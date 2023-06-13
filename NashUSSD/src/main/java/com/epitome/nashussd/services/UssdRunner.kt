package com.epitome.nashussd.services

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.epitome.nashussd.data.USSDPayload
import com.epitome.nashussd.exceptions.SHARED_PREFERENCE_NANE
import com.epitome.nashussd.interfaces.USSDExecutor
import com.epitome.nashussd.interfaces.UssdCallback
import com.epitome.nashussd.utils.AccessibilityHelper.isAccessibilityServiceEnabled
import com.epitome.nashussd.utils.AccessibilityHelper.isPhonePermissionGranted
import com.epitome.nashussd.utils.AccessibilityHelper.nashRequestPlaced

class UssdRunner(context: Context) : USSDExecutor {
    private val context: Context
    private val TAG = "USSDExecutor"

    private var prefs = context.getSharedPreferences(SHARED_PREFERENCE_NANE, Context.MODE_PRIVATE)
    private lateinit var ussdCallback: UssdCallback

    init {
        this.context = context
    }

    override fun execute(ussd: USSDPayload, callback: UssdCallback) {
        ussdCallback = callback
        when {
            !isPhonePermissionGranted(context) -> {
                Toast.makeText(context, "Please Allow the phone permission to run this request", Toast.LENGTH_LONG).show()
                return
            }
            !isAccessibilityServiceEnabled(context) -> {
                Toast.makeText(context, "Please Enable Nash service to run the this request", Toast.LENGTH_LONG).show()
                return
            }
            else -> {
                nashRequestPlaced = true // Initiated on start of request

                val editor = prefs.edit()
                editor.putInt("StepCount", 0) // reset the count
                editor.apply()

                Log.e(TAG, "Starting step count: ------> ${prefs.getInt("StepCount", 0)}")
                val ussdCode: String = ussd.code.toString().replace("#", ENCODED_HASH)
                val uri: Uri = Uri.parse("tel:$ussdCode")
                val intent = Intent(Intent.ACTION_CALL, uri)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            }
        }
    }

    override fun onResponse(result: String?) {
        Log.e(TAG, "Response: ------> $result")
    }

    override fun onComplete(result: String?) {
        ussdCallback.onSuccess(result.toString())
    }

    override fun onError(result: String?) {
        ussdCallback.onSuccess(result.toString())
    }

    companion object {
        private val ENCODED_HASH: String = Uri.encode("#")
    }
}