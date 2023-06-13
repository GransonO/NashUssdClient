package com.epitome.nashussd.services

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.epitome.nashussd.exceptions.INTERRUPTED_EXCEPTION
import com.epitome.nashussd.services.UssdService.Companion.ussdPromptFlow
import com.epitome.nashussd.utils.AccessibilityHelper.ussdExecutor
import com.epitome.nashussd.exceptions.NashClientException
import com.epitome.nashussd.exceptions.SHARED_PREFERENCE_NANE
import com.epitome.nashussd.services.UssdService.Companion.currentStep
import com.epitome.nashussd.utils.AccessibilityHelper.nashRequestPlaced
import com.epitome.nashussd.utils.DisallowedEventsEnum


class UssdService: AccessibilityService() {

    object Companion{
        var ussdPromptFlow = listOf<String>()
        var visibleActivityName: String = ""
        var currentStep = 0
    }

    private val TAG = "USSDService"

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {

        val prefs = getSharedPreferences(SHARED_PREFERENCE_NANE, Context.MODE_PRIVATE)
        val count = prefs.getInt("StepCount", 0)

        val rootNode = event!!.source

        try {
            if(nashRequestPlaced){
                // Request initiated from the app
                val ussdResponse = event.text
                currentStep = prefs.getInt("StepCount", 0)
                ussdExecutor?.onResponse(ussdResponse.toString())

                if(rootNode?.actionList != null){
                    val list: List<AccessibilityNodeInfo> = rootNode.findAccessibilityNodeInfosByText("SEND")
                    for (node in list) {
                        Log.e(TAG, "onAccessibilityEvent: ------------------ Current step $currentStep")

                        val nodeInput: AccessibilityNodeInfo = rootNode.findFocus(AccessibilityNodeInfo.FOCUS_INPUT)
                        val bundle = Bundle()
                        if(ussdPromptFlow[currentStep] == "PIN"){
                            // input the pin
                            // bundle.putCharSequence( AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, ussdPromptFlow[currentStep])
                            // nodeInput.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, bundle)
                        }else{
                            bundle.putCharSequence( AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, ussdPromptFlow[currentStep])
                            nodeInput.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, bundle)
                            nodeInput.refresh()

                            node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                        }
                    }


                    val okList: List<AccessibilityNodeInfo> = rootNode.findAccessibilityNodeInfosByText("OK")
                    for (node in okList) {
                        node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                        nashRequestPlaced = false
                        ussdExecutor?.onComplete("Completed $ussdResponse")
                        return
                    }
                }

                if(!event.text.contains(DisallowedEventsEnum.RUNNING.value) && !event.text.contains(DisallowedEventsEnum.SERVICE.value)){
                    val editor = prefs.edit()
                    editor.putInt("StepCount", count + 1)
                    editor.apply()

                    if(count + 1 > ussdPromptFlow.size){
                        // Completed request
                        nashRequestPlaced = false
                        ussdExecutor?.onComplete("Completed $ussdResponse")
                        return
                    }
                }
            }
        } catch (e: java.lang.Exception){
            if(rootNode?.actionList != null){
                val cancelList: List<AccessibilityNodeInfo> = rootNode.findAccessibilityNodeInfosByText("CANCEL")
                for (node in cancelList) {
                    node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
                    nashRequestPlaced = false
                    ussdExecutor?.onComplete("Completed with extra prompt: ${event.text}")
                    return
                }
            }
            // throw NashClientException("Flow out of bounds exception")
        }
    }

    override fun onInterrupt() {
        ussdExecutor?.onError(INTERRUPTED_EXCEPTION)
        nashRequestPlaced = false
        throw NashClientException(INTERRUPTED_EXCEPTION)
    }

}