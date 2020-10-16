package br.meetingplace.servicies.chat

import br.meetingplace.data.conversation.operations.ChatOperations
import br.meetingplace.data.conversation.ChatFullContent

class Chat(
    private var conversationId: String//the id of the conversation is the sum of the users id's
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

    fun deleteMessage(message: ChatOperations){

        if(message.idMessage in idMessages){
            val indexMessage = getMessageIndex(message.idMessage)
            conversation.remove(conversation[indexMessage])
            idMessages.remove(message.idMessage)

            if(message.idMessage in favoriteMessagesIds)
                unFavoriteMessage(message)
        }
    }

    fun favoriteMessage(message: ChatOperations){
        if(message.idMessage in idMessages )
            favoriteMessagesIds.add(message.idMessage)
    }

    fun unFavoriteMessage(message: ChatOperations){
        if(message.idMessage in idMessages)
            favoriteMessagesIds.remove(message.idMessage)
    }

    fun quoteMessage(message: ChatFullContent){
        val indexMessage = getMessageIndex(message.idMessage)
        if(message.idNewMessage !in idMessages && indexMessage != -1 && message.idMessage in idMessages){
            message.message = "|${conversation[indexMessage].message}|  "+ message.message
            val newMessage = Message(message.message, message.idNewMessage,true)
            println("LEVEL 3")
            addMessage(newMessage)
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