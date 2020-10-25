package br.meetingplace.services.group

import br.meetingplace.services.chat.Chat

class Group(){
    private var creator = ""
    private var name = "" // the name is the new ID
    private var idGroup = ""
    private var about= ""
    private val members = mutableListOf<Member>()
    private var chat = Chat.getChat()

    //GETTERS
    fun getCreator() = creator
    fun getNameGroup() = name
    fun getGroupId() = idGroup
    fun getAbout() = about
    fun getMembers() = members
    fun getChat()= chat
    //GETTERS

    fun updateChat(newChat: Chat){
        chat = newChat
    }

    fun startGroup (newName: String,newId: String ,about: String,newCreator: String){ // updates the ID
        if(name == "" && creator == "" && idGroup == ""){
            this.name = newName
            this.about = about
            idGroup = newId
            creator = newCreator
            chat.startChat(listOf(creator),"Group-Chat")
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
