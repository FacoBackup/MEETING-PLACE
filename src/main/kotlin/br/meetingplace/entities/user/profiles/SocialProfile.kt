package br.meetingplace.entities.user.profiles

import br.meetingplace.data.conversation.ChatContent
import br.meetingplace.servicies.chat.Chat
import br.meetingplace.servicies.notification.Inbox

class SocialProfile(){

    var userName = ""
    var gender= ""
    var nationality= ""
    var about= ""

    //private var conversationThread = mutableListOf<>()
    private var myThreads = 0
    private var chat = mutableListOf<Chat>()
    var followers = mutableListOf<Int>()
    var following = mutableListOf<Int>()
    var groups = mutableListOf<Int>()
    private var inbox = mutableListOf<Inbox>()
    // SUB METHODS

    fun updateInbox(notification: Inbox){
        inbox.add(notification)
    }

    fun startChat(conversation: Chat){
        chat.add(conversation)
    }

    fun newMessage(conversation: ChatContent, idChat: Int){

        val indexConversation = getChatIndex(idChat)
        if(idChat == chat[indexConversation].getId())
            chat[indexConversation].conversation.add(conversation.message)
    }

    fun getChatIndex(idChat: Int): Int{
        for(i in 0 until chat.size){
            if(chat[i].getId() == idChat)
                return i
        }
        return -1
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