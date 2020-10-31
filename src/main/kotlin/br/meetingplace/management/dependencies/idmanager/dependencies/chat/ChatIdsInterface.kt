package br.meetingplace.management.dependencies.idmanager.dependencies.chat

interface ChatIdsInterface {
    fun getChatId(firstId: String, secondId: String): String
}