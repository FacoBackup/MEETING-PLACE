package br.meetingplace.servicies.conversationThread

import br.meetingplace.data.ThreadContent

open class MainThread(){

    private var likes = mutableListOf<Int>() // Stores the IDs from the users who liked it
    private var dislikes = mutableListOf<Int>() // Stores the IDs from the users who disliked it
    private var creator = -1
    private var header = ""
    private var body = ""
    private var footer = "" // Receives the name of the logged user
    private var subThread = mutableListOf<SubThread>()
    private var id = -1

    //SETTERS
    fun startThread(content: ThreadContent, id: Int, userName: String, userId: Int){ // Updates the creator to the logged user
            creator = userId
            footer = userName
            header = content.title
            body = content.body
            this.id = id
    }

    fun like(Id: Int){
        likes.add(Id)
    }

    open fun dislike(Id: Int){
        dislikes.add(Id)
    }

    open fun addSubThread(sub: SubThread){
        if(sub.getCreator() != -1)
            subThread.add(sub)
    }
    //SETTERS

    //GETTERS
    open fun getLikes() = likes
    open fun getDislikes() = dislikes
    fun getId() = id
    open fun getLikeSize() = likes.size
    open fun getDislikeSize() = dislikes.size
    fun getContent() = listOf(header, body, footer)
    fun getCreator() = creator
    fun getSubthreads() = subThread
    //GETTERS
}