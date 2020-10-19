package br.meetingplace.servicies.groups

import br.meetingplace.servicies.chat.Chat

class Group(){
    private var creator = ""
    private var id = ""
    private var name = ""
    private var about= ""
    private val members = mutableListOf<Member>()
    private var chat = ""

    //GETTERS
    fun getCreator() = creator
    fun getNameGroup() = name
    fun getAbout() = about
    fun getId() = id
    fun getMembers() = members
    fun getChatId() = chat
    //GETTERS

    fun updateChat(newChatId: String){
        if(chat == "" && newChatId != "")
            chat = newChatId
    }

    fun startGroup (newId: String,name: String, about: String,newCreator: String){ // updates the ID
        if(id == "" && creator == ""){
            id = newId
            this.name = name
            this.about = about
            creator = newCreator
        }
    }

    fun updateMember(member: Member,remove: Boolean){
        when(remove){
            true ->{
                if(verifyMember(member.userEmail))
                    members.remove(member)
            }
            false->{
                if(!verifyMember(member.userEmail))
                    members.add(member)
            }
        }

    }

    fun getMemberRole(emailUser: String): Int{
        return if(verifyMember(emailUser) && emailUser != creator){
            val indexMember = getIndexMember(emailUser)
            members[indexMember].role
        }
        else if(emailUser != creator) 1
        else -1
    }

    private fun getIndexMember(emailUser: String): Int {
        for (i in 0 until members.size){
            if(members[i].userEmail == emailUser)
                return i
        }
        return -1
    }

    fun verifyMember(emailUser: String): Boolean {
        if(emailUser != creator){
            for (i in 0 until members.size){
                if(emailUser == members[i].userEmail)
                    return true
            }
            return false
        }
        else return emailUser == creator
    }
}
