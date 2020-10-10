package br.meetingplace.entities.grupos

import br.meetingplace.data.Member
import br.meetingplace.servicies.chat.Chat

open class Group(){

    private var creator = -1
    private var id = -1
    private var name = ""
    private var about= ""
    var members = mutableListOf<Member>()
    private val chat = Chat(-1)

    //GETTERS
    fun getCreator() = creator

    fun getNameGroup() = name

    fun getConversation() = chat.conversation

    fun getAbout() = about

    fun getChat() = chat

    fun getId() = id
    //GETTERS

    fun changeName(new: String){ // Needs work here
        name = new
    }

    fun updateId (new: Int){ // Needs work here
        if(id == -1)
            id = new
    }

    fun updateCreator (new: Int){ // Needs work here
        if(creator == -1)
            creator = new
    }

    fun sendMsg(message: String, sender: Int){
        if(verifyMember(sender))
            chat.conversation.add(message)
    }

    private fun verifyMember(idMember: Int): Boolean{
        if(idMember != creator){
            for(i in 0 until members.size){
                if(members[i].user == idMember)
                    return true
            }
            return false
        }
        else return true
    }

}
