package br.meetingplace.services.group

import br.meetingplace.services.chat.Chat

class Group(){
    private var creator = ""
    private var id = ""
    private var name = ""
    private var about= ""
    private val members = mutableListOf<Member>()
    private var chat = Chat("", listOf(creator))

    //GETTERS
    fun getCreator() = creator
    fun getNameGroup() = name
    fun getAbout() = about
    fun getId() = id
    fun getMembers() = members
    fun getChat()= chat
    //GETTERS

    fun updateChat(newChat: Chat){
        if(chat.getConversationId() != "" && newChat.getConversationId() == chat.getConversationId())
            chat = newChat
    }

    fun startGroup (newGroupId: String,newChatId: String, name: String, about: String,newCreator: String){ // updates the ID
        if(id == "" && creator == ""){
            id = newGroupId
            this.name = name
            this.about = about
            creator = newCreator
            chat.updateData(newChatId, listOf(creator))
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
