package br.meetingplace.entities.user.profiles

import br.meetingplace.data.conversation.ChatContent
import br.meetingplace.data.conversation.ChatFullContent
import br.meetingplace.data.conversation.operations.ChatOperations
import br.meetingplace.servicies.chat.Chat
import br.meetingplace.servicies.chat.Message
import br.meetingplace.servicies.notification.Inbox

class SocialProfile(
    private var userName: String,
    private var gender: String,
    private var nationality: String,
    private var about: String){

    //private var conversationThread = mutableListOf<>()
    private var myThreads = 0
    private var chat = mutableListOf<Chat>()
    private var chatIds = mutableListOf<Int>()
    var followers = mutableListOf<Int>()
    var following = mutableListOf<Int>()
    var groups = mutableListOf<Int>()
    private var inbox = mutableListOf<Inbox>()


    fun getUserName() = userName
    fun updateAbout(newAbout: String){
        about = newAbout
    }

    // SUB METHODS

    fun updateInbox(notification: Inbox){
        inbox.add(notification)
    }

    fun startChat(conversation: Chat){
        chat.add(conversation)
    }

    fun newMessage(message: Message, conversationId: Int){
        if(conversationId in chatIds){
            chat[getChatIndex(conversationId)].addMessage(message)
            chatIds.add(message.idMessage)
        }
    }

    fun deleteMessage(message: ChatOperations, conversationId: Int){
        if(conversationId in chatIds)
            chat[getChatIndex(conversationId)].deleteMessage(message)
    }

    fun favoriteMessage(message: ChatOperations, conversationId: Int){
        if(conversationId in chatIds)
            chat[getChatIndex(conversationId)].favoriteMessage(message)
    }

    fun unFavoriteMessage(message: ChatOperations, conversationId: Int){
        if(conversationId in chatIds)
            chat[getChatIndex(conversationId)].unFavoriteMessage(message)
    }

    fun quoteMessage(content: ChatFullContent, conversationId: Int){
        if(conversationId in chatIds)
            chat[getChatIndex(conversationId)].quoteMessage(content)
    }

    fun shareMessage(operations: ChatOperations, conversationId: Int): String {
        return if(conversationId in chatIds){
            chat[getChatIndex(conversationId)].shareMessage(operations)
        }
        else ""
    }

    fun getChatIndex(idChat: Int): Int{
        for(i in 0 until chatIds.size){
            if(chatIds[i] == idChat)
                return i
        }
        return -1
    }

    fun getChatById(idChat: Int): Chat {

        val indexChat = getChatIndex(idChat)
        val nullChat = Chat(-1)

        return if(indexChat != -1){
            chat[indexChat]
        }
        else nullChat
    }



    fun getMyThreadsQuantity () = myThreads
    fun updateMyThreadsQuantity (status: Boolean) {
        when(status){
            true->myThreads+=1
            false->myThreads-=1
        }
    }
    // SUB METHODS
}