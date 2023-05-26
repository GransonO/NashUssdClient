package com.tester.ussdclient.data

import com.epitome.nashussd.data.Ussd

interface UssdExecutor {
    fun run(ussd: Ussd?)
    fun setResponse(result: String?)
}