package br.meetingplace.entities.user.profiles

import br.meetingplace.data.Conversation
import br.meetingplace.entities.user.User
import br.meetingplace.servicies.chat.Chat
import br.meetingplace.servicies.conversationThread.MainThread
import br.meetingplace.servicies.conversationThread.SubThread
import br.meetingplace.servicies.notification.Inbox
import kotlin.concurrent.thread

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
    private var myThread = mutableListOf<MainThread>()
    private var threadId = mutableListOf<Int>()

    fun getThreadId ()=threadId

    fun getThreadIndex(idThread: Int): Int {

        if(idThread in threadId){

            for( i in 0 until myThread.size){
                if(myThread[i].getId() == idThread)
                    return i
            }
            return -1
        }
        else
            return -1
    }

    fun getThreadById(idThread: Int): MainThread {

        val nullThread= MainThread()
        val indexThread= getThreadIndex(idThread)

        return if (indexThread != -1)
            myThread[indexThread]
        else
            nullThread
    }

    fun threadOperations(indexThread: Int, operation: Int, idUser: Int){ //1 -> LIKE; 2 -> DISLIKE

        if (indexThread != -1){
            when(operation){
                1-> myThread[indexThread].like(idUser)
                2-> myThread[indexThread].dislike(idUser)
            }
        }
    }

    fun threadOperations(indexThread: Int, subThread: SubThread){

        if (indexThread != -1 && subThread.getCreator() != -1)
            myThread[indexThread].addSubThread(subThread)
    }

    // SUB METHODS
    fun updateInbox(notification: Inbox){
        inbox.add(notification)
    }

    fun addNewThread(new: MainThread){
        myThread.add(new)
        threadId.add(new.getId())
    }

    fun startChat(conversation: Chat){
        chat.add(conversation)
    }

    fun removeThread (indexThread: Int){
        if(indexThread != -1 && myThread.size > 0)
            myThread.remove(myThread[indexThread])
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