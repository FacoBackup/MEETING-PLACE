package br.meetingplace.entities.user.profiles

import br.meetingplace.data.conversation.Conversation
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
    private var myThread = mutableListOf<MainThread>()
    private var myThreadId = mutableListOf<Int>()
    private var timeline = mutableListOf<MainThread>()
    private var timelineId = mutableListOf<Int>()

    // SUB METHODS

    fun updateInbox(notification: Inbox){
        inbox.add(notification)
    }

    fun addMyThread(thread:MainThread){
        val indexThread = getMyThreadIndex(thread.getId())
        if (indexThread == -1 ){
            myThread.add(thread)
            myThreadId.add(thread.getId())
        }
    }

    fun removeMyThread(thread:MainThread){
        val indexThread = getMyThreadIndex(thread.getId())
        if (indexThread != -1 && indexThread in myThreadId){
            myThread.remove(thread)
            myThreadId.remove(thread.getId())
        }
    }

    fun updateMyThreads(thread:MainThread){
        val indexThread = getMyThreadIndex(thread.getId())
        if (indexThread != -1 && indexThread in myThreadId){
            myThread.remove(myThread[indexThread])
            myThread.add(thread)
        }
    }

    fun addTimelineThread(thread:MainThread){
        val indexThread = getTimelineThreadIndex(thread.getId())
        if (indexThread != -1 && indexThread in timelineId){
            timeline.add(thread)
            timelineId.add(thread.getId())
        }
    }
    fun removeTimelineThread(thread:MainThread){
        val indexThread = getTimelineThreadIndex(thread.getId())
        if (indexThread != -1 && indexThread in timelineId){
            timeline.remove(thread)
            timelineId.remove(thread.getId())
        }
    }

    fun updateTimeline(thread: MainThread){
        val indexThread = getTimelineThreadIndex(thread.getId())
        if (indexThread != -1 && indexThread in timelineId){
            timeline.remove(timeline[indexThread])
            timeline.add(thread)
        }
    }

    fun startChat(conversation: Chat){
        chat.add(conversation)
    }

    fun newMessage(conversation: Conversation, idChat: Int){

        val indexConversation = getChatIndex(idChat)
        if(idChat == chat[indexConversation].getId())
            chat[indexConversation].conversation.add(conversation.message)
    }

    fun getMyThreadId() = myThreadId
    fun getTimelineThreadId() = timelineId

    fun getChatIndex(idChat: Int): Int{
        for(i in 0 until chat.size){
            if(chat[i].getId() == idChat)
                return i
        }
        return -1
    }

    private fun getMyThreadIndex(idThread: Int): Int {

        if(idThread in myThreadId){

            for( i in 0 until myThread.size){
                if(myThread[i].getId() == idThread)
                    return i
            }
            return -1
        }
        else
            return -1
    }
    fun getTimelineThreadIndex(idThread: Int): Int {

        if(idThread in timelineId){

            for( i in 0 until myThread.size){
                if(myThread[i].getId() == idThread)
                    return i
            }
            return -1
        }
        else
            return -1
    }
    // SUB METHODS
}