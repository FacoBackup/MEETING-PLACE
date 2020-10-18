package br.meetingplace.interfaces

interface Message {
    fun getChatId(firstId: String, secondId: String): String {
        return firstId.removeSuffix("@") + secondId.removeSuffix("@")
    }
}