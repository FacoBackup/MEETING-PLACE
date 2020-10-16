package br.meetingplace.management.interfaces

import java.util.*

interface Generator {
    fun generateId(): String {
        return UUID.randomUUID().toString()
    }
}