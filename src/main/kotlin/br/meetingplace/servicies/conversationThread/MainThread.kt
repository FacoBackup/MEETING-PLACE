package br.meetingplace.servicies.conversationThread

import br.meetingplace.data.threads.ThreadContent

open class MainThread(){

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

    fun like(IdUser: Int){
        likes.add(IdUser)
    }

    fun dislike(IdUser: Int){
        dislikes.add(IdUser)
    }

    fun likeToDislike(userId: Int){
        if(userId in likes){
            likes.remove(userId)
            dislikes.add(userId)
        }
    }

    fun dislikeToLike(userId: Int) {
        if(userId in dislikes){
            dislikes.remove(userId)
            likes.add(userId)
        }
    }

    open fun likeSubThread(idUser: Int, idSubThread: Int){

        if(idSubThread in subThreadId){
            val indexSubThread = getSubThreadIndex(idSubThread)
            subThread[indexSubThread].likes.add(idUser)
        }
    }

    open fun dislikeSubThread(idUser: Int, idSubThread: Int){

        if(idSubThread in subThreadId){
            val indexSubThread = getSubThreadIndex(idSubThread)
            subThread[indexSubThread].dislikes.add(idUser)
        }
    }

    open fun likeToDislikeSubThread(idUser: Int, idSubThread: Int){
        if(idSubThread in subThreadId){
            val indexSubThread = getSubThreadIndex(idSubThread)
            subThread[indexSubThread].likes.remove(idUser)
            subThread[indexSubThread].dislikes.add(idUser)
        }
    }

    open fun dislikeToLikeSubThread(idUser: Int, idSubThread: Int) {
        if(idSubThread in subThreadId){
            val indexSubThread = getSubThreadIndex(idSubThread)

            subThread[indexSubThread].dislikes.remove(idUser)
            subThread[indexSubThread].likes.add(idUser)
        }
    }

    open fun addSubThread(sub: SubThread){
        if(sub.creator != -1){
            subThreadId.add(sub.id)
            subThread.add(sub)
        }
    }

    open fun removeSubThread(idSubThread: Int, idCreator: Int){

        val indexSubThread = getSubThreadIndex(idSubThread)
        if(idSubThread in subThreadId && idCreator == subThread[indexSubThread].creator){

            subThreadId.remove(subThread[indexSubThread].id)
            subThread.remove(subThread[indexSubThread])
        }
    }

    fun getSubThreadById(idSubThread: Int): SubThread {
        val nullSubThread = SubThread(mutableListOf(), mutableListOf(), -1, "", "", "", -1)
        return if(idSubThread in subThreadId){
            val indexSubThread = getSubThreadIndex(idSubThread)
            subThread[indexSubThread]
        }
        else nullSubThread
    }
    //SETTERS

    //GETTERS
    private fun getSubThreadIndex(id: Int): Int {

        if(id in subThreadId){
            for(i in 0 until subThread.size){
                if (subThread[i].id == id)
                    return i
            }
            return -1
        }
        else return -1
    }

    fun getSubThreadCreator(idSubThread: Int): Int {
        val indexSubThread = getSubThreadIndex(idSubThread)
        return if(indexSubThread != -1)
            subThread[indexSubThread].creator
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