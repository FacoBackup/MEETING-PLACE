package br.meetingplace.servicies.chat

import br.meetingplace.data.conversation.operations.ChatOperations
import br.meetingplace.data.conversation.ChatContent

class Chat(
    private var conversationId: Int//the id of the conversation is the sum of the users id's
){

    private var conversation = mutableListOf<Message>()
    private var idMessages= mutableListOf<Int>()
    private var favoriteMessagesIds= mutableListOf<Int>()

    fun addMessage(message: Message){
        if(message.idMessage !in idMessages){
            conversation.add(message)
            idMessages.add(message.idMessage)
        }
    }

    fun deleteMessage(message: ChatOperations, loggedUser: Int){
        if(message.idMessage in idMessages && message.receiver + loggedUser == conversationId){
            val indexMessage = getMessageIndex(message.idMessage)
            conversation.remove(conversation[indexMessage])
            idMessages.remove(message.idMessage)
            if(message.idMessage in favoriteMessagesIds)
                unFavoriteMessage(message, loggedUser)
        }
    }

    fun favoriteMessage(message: ChatOperations, loggedUser: Int){
        if(message.idMessage in idMessages && message.receiver + loggedUser == conversationId)
            favoriteMessagesIds.add(message.idMessage)
    }

    fun unFavoriteMessage(message: ChatOperations, loggedUser: Int){
        if(message.idMessage in idMessages && message.receiver + loggedUser == conversationId)
            favoriteMessagesIds.remove(message.idMessage)
    }

    fun quoteMessage(message: Message, operations: ChatOperations){
        val indexMessage = getMessageIndex(operations.idMessage)
        if(message.idMessage !in idMessages && indexMessage != -1){
            message.message = "|Quoting ${conversation[indexMessage].message}\n"+ message.message
            addMessage(message)
        }
    }

    fun shareMessage(operations: ChatOperations): String {
        val indexMessage = getMessageIndex(operations.idMessage)
        return if(indexMessage != -1)
            conversation[indexMessage].message
        else ""
    }

    //GETTERS
    private fun getMessageIndex(id: Int): Int {
        for (i in 0 until idMessages.size){
            if(idMessages[i] == id)
                return i
        }
        return -1
    }

    fun getConversation ()= conversation
    fun getFavoriteMessagesIds() = favoriteMessagesIds
    fun getMessageIds() = idMessages
    fun getConversationId() = conversationId
    //GETTERS
}