package com.epitome.nashussd.data

data class Ussd(
     var id: String? = null,
     var code: String? = null,
     var response: String? = null
){

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
