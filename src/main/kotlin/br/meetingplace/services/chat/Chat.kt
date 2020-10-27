package br.meetingplace.services.chat

import br.meetingplace.data.chat.ChatComplexOperations
import br.meetingplace.data.chat.ChatOperations

class Chat private constructor(){
    private var id= ""
    private var owners = mutableListOf<String>()
    private var messages = mutableListOf<Message>()
    private var idMessages= mutableListOf<String>()
    private var favoriteMessages= mutableListOf<String>()

    companion object{
        private val chat = Chat()
        fun getChat() = chat
    }

    fun startChat(owners: List<String>, id: String){

        if(this.id == "" && this.owners.isEmpty()){
            this.owners.addAll(owners)
            this.id = id
        }
    }

    fun addMessage(message: Message){
        if(message.idMessage !in idMessages){
            messages.add(message)
            idMessages.add(message.idMessage)
        }
    }

    fun deleteMessage(message: ChatOperations){

        if(message.idMessage in idMessages){
            if(message.idMessage in favoriteMessages)
                unFavoriteMessage(message)
            val indexMessage = getMessageIndex(message.idMessage)
            messages.remove(messages[indexMessage])
            idMessages.remove(message.idMessage)
        }
    }

    fun favoriteMessage(message: ChatOperations){
        if(message.idMessage in idMessages && message.idMessage !in favoriteMessages)
            favoriteMessages.add(message.idMessage)
    }

    fun unFavoriteMessage(message: ChatOperations){
        if(message.idMessage in idMessages && message.idMessage in favoriteMessages)
            favoriteMessages.remove(message.idMessage)
    }

    fun quoteMessage(message: ChatComplexOperations, newId: String){
        val indexMessage = getMessageIndex(message.idMessage)
        if(indexMessage != -1){
            message.message = "|${messages[indexMessage].message}|  "+ message.message
            val newMessage = Message(message.message, newId,messages[indexMessage].creator, true)
            addMessage(newMessage)
        }
    }


    fun shareMessage(operations: ChatComplexOperations): String{
        val indexMessage = getMessageIndex(operations.idMessage)
        return if(indexMessage != -1)
            messages[indexMessage].message
        else ""
    }

    //GETTERS
    private fun getMessageIndex(id: String): Int {
        for (i in 0 until idMessages.size){
            if(idMessages[i] == id)
                return i
        }
        return -1
    }

    fun verifyMessage(idMessage: String): Boolean {
        return idMessage in idMessages
    }
    fun getOwners() = owners
    fun getConversation ()= messages
    fun getFavoriteMessagesIds() = favoriteMessages
    fun getMessageIds() = idMessages
    fun getConversationId() = id
    //GETTERS
}