package com.epitome.nashussd.interfaces

interface UssdCallback {
    fun onSuccess(value: String)
    fun onFailure(value: String)
}