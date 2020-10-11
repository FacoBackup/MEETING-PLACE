package br.meetingplace.servicies.conversationThread

import br.meetingplace.servicies.management.GeneralEntitiesManagement

open class MainThread(): GeneralEntitiesManagement(){

    private var like = mutableListOf<Int>() // Stores the IDs from the users who liked it
    private var dislike = mutableListOf<Int>() // Stores the IDs from the users who disliked it
    private var creator = -1
    private var header = ""
    private var body = ""
    private var footer = "" // Receives the name of the logged user
    private var subThread = mutableListOf<SubThread>()

    //SETTERS
    fun startThread(){ // Updates the creator to the logged user
        if(getLoggedUser() != -1){
            val name = getSocialNameById(getLoggedUser())
            creator = getLoggedUser()
            footer = name
        }
    }

    fun like(Id: Int){
        if(getLoggedUser() != -1 && verifyUser(Id))
            like.add(Id)
    }

    fun dislike(Id: Int){
        if(getLoggedUser() != -1 && verifyUser(Id))
            dislike.add(Id)
    }

    fun addSubThread(sub: SubThread){
        if(sub.getCreator() != -1)
            subThread.add(sub)
    }
    //SETTERS

    //GETTERS
    fun getLikes() = like.size
    fun getDislikes() = dislike.size
    fun getContent() = listOf(header, body, footer)
    fun getCreator() = creator
    fun getSubthreads() = subThread
    //GETTERS
}