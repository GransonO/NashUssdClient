package com.epitome.nashussd.data

data class USSDPayload(
     var id: String? = null,
     var code: String? = null,
     var response: String? = null,
     var hasPin: Boolean = false,
     var promptFlow: String = ""
)
