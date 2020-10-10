package br.meetingplace.entities.user.profiles

import br.meetingplace.data.Conversation
import br.meetingplace.entities.user.User
import br.meetingplace.servicies.chat.Chat
import br.meetingplace.servicies.notification.Inbox
import br.meetingplace.servicies.publication.Post

class SocialProfile(): User(){

    var userName = ""
    var gender= ""
    var nacionality= ""
    var about= ""

    private var posts = mutableListOf<Post>()
    private var chat = mutableListOf<Chat>()
    var followers = mutableListOf<Int>()
    var following = mutableListOf<Int>()
    var groups = mutableListOf<Int>()
    private var inbox = mutableListOf<Inbox>()



    // SUB METHODS

    fun makePost()

    fun updateInbox(notification: Inbox){
        inbox.add(notification)
    }

    fun startChat(conversation: Chat){
        chat.add(conversation)
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