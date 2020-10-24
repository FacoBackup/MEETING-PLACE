package br.meetingplace.management.core.operators

import java.util.*

interface Generator {
    fun generateId(): String {
        return UUID.randomUUID().toString()
    }
    fun getChatId(firstId: String, secondId: String): String {
        return firstId.removeSuffix("@") + secondId.removeSuffix("@")
    }
}