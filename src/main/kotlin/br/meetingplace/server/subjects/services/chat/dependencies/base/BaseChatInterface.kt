package br.meetingplace.server.subjects.services.chat.dependencies.base

import br.meetingplace.server.dto.chat.ChatComplexOperator
import br.meetingplace.server.dto.chat.ChatSimpleOperator
import br.meetingplace.server.subjects.services.chat.dependencies.data.MessageContent

interface BaseChatInterface {
    fun addMessage(message: MessageContent)
    fun deleteMessage(message: ChatSimpleOperator)
    fun shareMessage(operations: ChatComplexOperator): String?
    fun quoteMessage(message: ChatComplexOperator, newId: String)
    fun favoriteMessage(message: ChatSimpleOperator)
    fun disfavorMessage(message: ChatSimpleOperator)
    fun verifyMessage(idMessage: String): Boolean
}