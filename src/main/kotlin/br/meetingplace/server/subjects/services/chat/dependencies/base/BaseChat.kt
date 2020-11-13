package br.meetingplace.server.subjects.services.chat.dependencies.base

import br.meetingplace.server.dto.chat.ChatComplexOperator
import br.meetingplace.server.dto.chat.ChatSimpleOperator
import br.meetingplace.server.subjects.services.chat.dependencies.data.MessageContent
import br.meetingplace.server.subjects.services.chat.dependencies.data.MessageType

class BaseChat private constructor() : BaseChatInterface {
    companion object {
        private val Class = BaseChat()
        fun getClass() = Class
    }

    private var messages = mutableListOf<MessageContent>()
    private var idMessages = mutableListOf<String>()
    private var favoriteMessages = mutableListOf<String>()


    override fun verifyMessage(idMessage: String): Boolean {
        return idMessage in idMessages
    }

    override fun addMessage(message: MessageContent) {
        if (message.messageID !in idMessages) {
            messages.add(message)
            idMessages.add(message.messageID)
        }
    }

    override fun deleteMessage(message: ChatSimpleOperator) {
        if (message.messageID in idMessages) {
            if (message.messageID in favoriteMessages)
                disfavorMessage(message)
            messages.removeAt(idMessages.indexOf(message.messageID))
            idMessages.remove(message.messageID)
        }
    }

    override fun shareMessage(operations: ChatComplexOperator): String? {
        val index = idMessages.indexOf(operations.messageID)
        return if (index != -1)
            messages[index].message
        else null
    }

    override fun quoteMessage(message: ChatComplexOperator, newId: String) {
        val index = idMessages.indexOf(message.messageID)
        if (index != -1) {
            message.message = "|${messages[index].message}|  " + message.message
            val newMessage = MessageContent(message.message, newId, messages[index].creator, MessageType.QUOTED)
            addMessage(newMessage)
        }
    }

    override fun favoriteMessage(message: ChatSimpleOperator) {
        if (message.messageID in idMessages && message.messageID !in favoriteMessages)
            favoriteMessages.add(message.messageID)
    }

    override fun disfavorMessage(message: ChatSimpleOperator) {
        if (message.messageID in idMessages && message.messageID in favoriteMessages)
            favoriteMessages.remove(message.messageID)
    }
}