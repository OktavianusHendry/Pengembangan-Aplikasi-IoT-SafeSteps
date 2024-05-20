package com.okta.iottest.model

import com.google.firebase.Timestamp

data class NotificationData(
    val title: String? = "Title",
    val message: String? = "Message",
    val timestamp: Timestamp? = Timestamp.now(),
)