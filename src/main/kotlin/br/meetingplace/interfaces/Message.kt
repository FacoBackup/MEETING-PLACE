package br.meetingplace.interfaces

import br.meetingplace.data.conversation.ChatContent
import br.meetingplace.data.conversation.ChatFullContent
import br.meetingplace.data.conversation.operations.ChatOperations

interface Message {
    fun sendMessage(message: ChatContent)
    fun deleteMessage(message: ChatOperations)
    fun favoriteMessage(message: ChatOperations)
    fun unFavoriteMessage(message: ChatOperations)
    fun quoteMessage(message: ChatFullContent)
    fun shareMessage(content: ChatFullContent)
}