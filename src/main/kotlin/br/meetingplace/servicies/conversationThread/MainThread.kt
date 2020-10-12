package br.meetingplace.servicies.conversationThread

import br.meetingplace.data.ThreadContent

class MainThread(){

    private var likes = mutableListOf<Int>() // Stores the IDs from the users who liked it
    private var dislikes = mutableListOf<Int>() // Stores the IDs from the users who disliked it
    private var creator = -1
    private var header = ""
    private var body = ""
    private var footer = "" // Receives the name of the logged user
    private var subThread = mutableListOf<SubThread>()
    private var subThreadId = mutableListOf<Int>()
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

    fun dislike(Id: Int){
        dislikes.add(Id)
    }

    fun addSubThread(sub: SubThread){
        if(sub.getCreator() != -1){
            subThreadId.add(sub.getId())
            subThread.add(sub)
        }

    }
    fun removeSubThread(idSubThread: Int, idCreator: Int){

        val indexSubThread = getSubThreadIndex(idSubThread)
        if(indexSubThread != -1 && idCreator == subThread[indexSubThread].getId()){
            subThreadId.remove(subThread[indexSubThread].getId())
            subThread.remove(subThread[indexSubThread])
        }
    }
    //SETTERS

    //GETTERS
    private fun getSubThreadIndex(id: Int): Int {

        if(id in subThreadId){
            for(i in 0 until subThread.size){
                if (subThread[i].getId() == id)
                    return i
            }
            return -1
        }
        else return -1
    }
    fun getSubThreadCreator(id: Int): Int {

        val indexSubThread = getSubThreadIndex(id)
        return if(indexSubThread != -1)
            subThread[indexSubThread].getCreator()
        else
            -1
    }
    fun getLikes() = likes
    fun getDislikes() = dislikes
    fun getId() = id
    fun getLikeSize() = likes.size
    fun getDislikeSize() = dislikes.size
    fun getContent() = listOf(header, body, footer)
    fun getCreator() = creator
    fun getSubThreadsId () = subThreadId
    //GETTERS
}