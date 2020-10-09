package br.meetingplace.entities.grupos

import br.meetingplace.data.GroupConversation
import br.meetingplace.data.Member
import br.meetingplace.management.EntitiesManagement
import br.meetingplace.servicies.Authentication
import br.meetingplace.servicies.chat.Chat

open class Group(): Authentication(){

    private var creator = -1
    private var id = -1
    private var name = ""
    private var about: String? = null
    var members = mutableListOf<Member>()
    private var chat = Chat()


    //GETTERS
    fun getCreator() = creator

    fun getNameGroup() = name

    fun getConversation() = chat.conversation

    fun getAbout() = about

    fun getChat() = chat

    fun getId() = id
    //GETTERS

    fun changeName(new: String){
        name = new
    }

    fun updateId (new: Int){
        if(id == -1)
            id = new
    }

    fun updateCreator (new: Int){
        if(creator == -1)
            creator = new
    }

    fun sendMsg(Conversation: GroupConversation, Verifier: Int){

        if(Verifier == getLoggedUser()){
            if(chat.id == -1){
                chat.id = id
                chat.conversation.add(Conversation.message)
            }
            else
                chat.conversation.add(Conversation.message)
        }
    }
}
