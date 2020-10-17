package br.meetingplace.interfaces

interface Message {
    fun getChatId(fistId: String, secondId: String): String {
        val newId = fistId.chunked(8) + secondId.chunked(8)
        return newId[0] + newId[5]
    }
}