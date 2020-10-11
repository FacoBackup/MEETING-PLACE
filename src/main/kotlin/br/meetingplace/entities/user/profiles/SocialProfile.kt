package br.meetingplace.entities.user.profiles

import br.meetingplace.data.Conversation
import br.meetingplace.entities.user.User
import br.meetingplace.servicies.chat.Chat
import br.meetingplace.servicies.conversationThread.MainThread
import br.meetingplace.servicies.notification.Inbox

class SocialProfile(){

    var userName = ""
    var gender= ""
    var nacionality= ""
    var about= ""

    //private var conversationThread = mutableListOf<>()
    private var chat = mutableListOf<Chat>()
    var followers = mutableListOf<Int>()
    var following = mutableListOf<Int>()
    var groups = mutableListOf<Int>()
    private var inbox = mutableListOf<Inbox>()
    private var myThreads = mutableListOf<MainThread>()


    fun getThreads() = myThreads

    // SUB METHODS
    fun updateInbox(notification: Inbox){
        inbox.add(notification)
    }

    fun addNewThread(new: MainThread){
        myThreads.add(new)
    }

    fun startChat(conversation: Chat){
        chat.add(conversation)
    }

    fun removeThread (indexThread: Int){
        if(indexThread != -1 && myThreads.size > 0)
            myThreads.remove(myThreads[indexThread])
    }

    fun updateChat(conversation: Conversation, idChat: Int){

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
    // SUB METHODS
}