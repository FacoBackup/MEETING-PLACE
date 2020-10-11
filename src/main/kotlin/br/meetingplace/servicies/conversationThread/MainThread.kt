package br.meetingplace.servicies.conversationThread

import br.meetingplace.data.ThreadContent
import br.meetingplace.servicies.management.GeneralEntitiesManagement

open class MainThread(){

    private var like = mutableListOf<Int>() // Stores the IDs from the users who liked it
    private var dislike = mutableListOf<Int>() // Stores the IDs from the users who disliked it
    private var creator = -1
    private var header = ""
    private var body = ""
    private var footer = "" // Receives the name of the logged user
    private var subThread = mutableListOf<SubThread>()
    private var id = -1

    //SETTERS
    fun startThread(content: ThreadContent, id: Int, userName: String, userId: Int){ // Updates the creator to the logged user
            val name = userName
            creator = userId
            footer = name
            header = content.title
            body = content.body
            this.id = id
    }

    fun like(Id: Int){
        like.add(Id)
    }

    fun dislike(Id: Int){
        dislike.add(Id)
    }

    fun addSubThread(sub: SubThread){
        if(sub.getCreator() != -1)
            subThread.add(sub)
    }
    //SETTERS

    //GETTERS
    fun getId() = id
    fun getLikes() = like.size
    fun getDislikes() = dislike.size
    fun getContent() = listOf(header, body, footer)
    fun getCreator() = creator
    fun getSubthreads() = subThread
    //GETTERS
}