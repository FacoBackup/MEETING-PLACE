package br.meetingplace.servicies.groups

import br.meetingplace.data.group.Member

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

    fun startGroup (newId: String, newCreator: String){ // updates the ID
        if(id == "" && creator == ""){
            id = newId
            creator = newCreator
        }
    }
}
