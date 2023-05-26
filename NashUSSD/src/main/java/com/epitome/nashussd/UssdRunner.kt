package com.epitome.nashussd

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.epitome.nashussd.data.Ussd
import com.tester.ussdclient.data.UssdExecutor
import com.epitome.nashussd.utils.AccessibilityHelper


class UssdRunner(context: Context) : UssdExecutor {
    private val context: Context
    private var pendingUssd: Ussd? = null
    val TAG = "UssdExecutor"

    init {
        this.context = context
    }

    @SuppressLint("MissingPermission")
    override fun run(ussd: Ussd?) {
        if (!AccessibilityHelper.isPermissionGranted(context)) {
            return
        }
        val ussdCode: String = ussd?.code.toString().replace("#", ENCODED_HASH)
        val uri: Uri = Uri.parse("tel:$ussdCode")
        val intent = Intent(Intent.ACTION_CALL, uri)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)
    }

    override fun setResponse(result: String?) {
        Log.e(TAG, "setResponse: -------------------------------> $result")
    }

    companion object {
        private val ENCODED_HASH: String = Uri.encode("#")
    }
}