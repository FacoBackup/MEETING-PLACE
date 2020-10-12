package br.meetingplace.servicies.conversationThread

import br.meetingplace.data.ThreadContent

class SubThread{

    private var creator = -1
    private var header = ""
    private var body = ""
    private var footer = "" // Receives the name of the logged user
    private var id = -1

    //SETTERS
    fun startThread(content: ThreadContent, id: Int, userName: String, userId: Int){ // Updates the creator to the logged user
        creator = userId
        footer = userName
        header = content.title
        body = content.body
        this.id = id
    }
    //SETTERS

    //GETTERS
    fun getId() = id
    fun getCreator() = creator
    //GETTERS

}