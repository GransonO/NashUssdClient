package com.epitome.nashussd.services

import android.accessibilityservice.AccessibilityService
import android.os.Bundle
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.epitome.nashussd.utils.AccessibilityHelper.ussdExecutor


class UssdService: AccessibilityService() {

    private val TAG = "UssdService"
    private val currentFlow = listOf("1", "2", "1")
    var currentStep = 0

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        Log.e(TAG, "onAccessibilityEvent: ------------------ Success")
        Log.e(TAG, "onAccessibilityEvent: eventType ------------------ ${event?.eventType}")
        Log.e(TAG, "onAccessibilityEvent: recordCount ------------------ ${event?.recordCount}")
        Log.e(TAG, "onAccessibilityEvent: contentDescription ------------------ ${event?.contentDescription}")
        Log.e(TAG, "onAccessibilityEvent: text ------------------ ${event?.text}")

        val rootNode = event!!.source
        Log.e(TAG, "onAccessibilityEvent: rootNode.actionList ------------------ ${rootNode?.actionList}")

        val ussdResponse = event.text
        ussdExecutor?.setResponse(ussdResponse.toString())

        if(rootNode?.actionList != null){
            val list: List<AccessibilityNodeInfo> = rootNode.findAccessibilityNodeInfosByText("SEND")
            for (node in list) {
                Log.e(TAG, "onAccessibilityEvent: ------------------ Send button present")
                val nodeInput: AccessibilityNodeInfo = rootNode.findFocus(AccessibilityNodeInfo.FOCUS_INPUT)
                val bundle = Bundle()
                bundle.putCharSequence( AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, currentFlow[currentStep])
                nodeInput.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, bundle)
                nodeInput.refresh()

                node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                currentStep = +1
            }
        }
    }

    override fun onInterrupt() {
        Log.e(TAG, "onInterrupt: ------------------ Error")
    }
}