package br.meetingplace.management.core.chat.dependencies

import br.meetingplace.data.chat.ChatComplexOperations
import br.meetingplace.data.chat.ChatMessage
import br.meetingplace.data.chat.ChatOperations

interface ChatInterface {
    fun sendMessage(data: ChatMessage)
    fun favoriteMessage(data: ChatOperations)
    fun unFavoriteMessage(data: ChatOperations)
    fun quoteMessage(data: ChatComplexOperations)
    fun shareMessage(data: ChatComplexOperations)
    fun deleteMessage(data: ChatOperations)
}