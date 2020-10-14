package br.meetingplace.servicies.chat

import br.meetingplace.data.conversation.operations.ChatOperations
import br.meetingplace.data.conversation.ChatContent

class Chat(ID: Int){
    private var conversation = mutableListOf<Message>()
    private var conversationId = ID //the id of the conversation is the sum of the users id's
    private var messageIds= mutableListOf<Int>()
    private var favoriteMessagesIds= mutableListOf<Int>()

    fun getConversation ()= conversation

    fun addMessage(message: ChatContent){

    }

    fun deleteMessage(message: ChatOperations){

    }

    fun favoriteMessage(message: ChatOperations){

    }

    fun quoteMessage(message: ChatOperations){

    }

    fun shareMessage(message: ChatOperations){

    }

    //GETTERS
    fun getFavoriteMessagesIds() = favoriteMessagesIds
    fun getMessageIds() = messageIds
    fun getConversationId() = conversationId
    //GETTERS
}