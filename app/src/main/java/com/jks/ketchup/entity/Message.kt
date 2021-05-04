package com.jks.ketchup.entity

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class Message(
        val messageText: String = "",
        val fromUid: String = "",
        @ServerTimestamp
        val sentAt: Date? = null
)