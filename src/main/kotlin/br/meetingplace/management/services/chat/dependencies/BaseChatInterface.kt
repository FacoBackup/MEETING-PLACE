package br.meetingplace.management.services.chat.dependencies

import br.meetingplace.data.chat.ChatMessage
import br.meetingplace.data.chat.ChatOperations

interface BaseChatInterface {
    fun sendMessage(data: ChatMessage)
    fun deleteMessage(data: ChatOperations)
}