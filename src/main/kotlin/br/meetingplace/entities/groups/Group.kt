package br.meetingplace.entities.groups

import br.meetingplace.data.entities.group.Member
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

//    fun getConversation() = chat.conversation

    fun getAbout() = about

    fun getChat() = chat

    fun getId() = id
    //GETTERS

    /*
    fun changeName(new: String){ // Needs work here
        name = new
    }

    fun startGroup (newId: Int, newCreator: Int){ // updates the ID
        if(id == -1 && creator == -1){
            id = newId
            creator = newCreator
        }

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
    */
}
