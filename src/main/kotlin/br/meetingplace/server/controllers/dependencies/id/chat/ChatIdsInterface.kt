package br.meetingplace.server.controllers.dependencies.id.chat

interface ChatIdsInterface {
    fun getChatId(firstId: String, secondId: String): String
}