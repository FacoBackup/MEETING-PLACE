package br.meetingplace.entities.user.profiles

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
    private var chatIds = mutableListOf<String>()
    var followers = mutableListOf<String>()
    var following = mutableListOf<String>()
    var groups = mutableListOf<String>()
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
        chatIds.add(conversation.getConversationId())
    }

    fun newMessage(message: Message, conversationId: String){
        if(conversationId in chatIds){
            chat[getChatIndex(conversationId)].addMessage(message)
            chatIds.add(message.idMessage)
        }
    }

    fun deleteMessage(message: ChatOperations, conversationId: String){
        if(conversationId in chatIds){

            chat[getChatIndex(conversationId)].deleteMessage(message)
        }

    }

    fun favoriteMessage(message: ChatOperations, conversationId: String){
        if(conversationId in chatIds)
            chat[getChatIndex(conversationId)].favoriteMessage(message)
    }

    fun unFavoriteMessage(message: ChatOperations, conversationId: String){
        if(conversationId in chatIds)
            chat[getChatIndex(conversationId)].unFavoriteMessage(message)
    }

    fun quoteMessage(content: ChatFullContent, conversationId: String){
        if(conversationId in chatIds){
            println("LEVEL 2")
            chat[getChatIndex(conversationId)].quoteMessage(content)
        }

    }

    fun shareMessage(operations: ChatOperations, conversationId: String): String {
        return if(conversationId in chatIds){
            chat[getChatIndex(conversationId)].shareMessage(operations)
        }
        else ""
    }

    fun getChatIndex(idChat: String): Int{
        for(i in 0 until chat.size){
            if(chat[i].getConversationId() == idChat)
                return i
        }
        return -1
    }

    fun getChatById(idChat: String): Chat {

        val indexChat = getChatIndex(idChat)
        val nullChat = Chat("")

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