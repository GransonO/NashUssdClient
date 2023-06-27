package com.epitome.nashussd.interfaces

import com.epitome.nashussd.data.USSDPayload

interface USSDExecutor {
    fun execute(ussd: USSDPayload, callback: UssdCallback)
    fun onComplete(result: String?)
    fun onError(result: String?)
}