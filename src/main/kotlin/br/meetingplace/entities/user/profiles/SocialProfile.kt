package br.meetingplace.entities.user.profiles

import br.meetingplace.data.conversation.ChatContent
import br.meetingplace.servicies.chat.Chat
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
    
    fun getMyThreadsQuantity () = myThreads
    fun updateMyThreadsQuantity (status: Boolean) {
        when(status){
            true->myThreads+=1
            false->myThreads-=1
        }
    }
    // SUB METHODS
}