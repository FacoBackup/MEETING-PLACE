package br.meetingplace.services.thread

import br.meetingplace.data.threads.ThreadData
import br.meetingplace.services.community.Community


class MainThread(){

    private var likes = mutableListOf<String>() // Stores the IDs from the users who liked it
    private var dislikes = mutableListOf<String>() // Stores the IDs from the users who disliked it
    private var creator = ""
    private var header = ""
    private var body = ""
    private var footer = "" // Receives the name of the logged user
    private var subThread = mutableListOf<SubThread>()
    private var subThreadId = mutableListOf<String>()
    private var id = ""
    private var community: String? = null
    //SETTERS

    fun updateCommunity(idCommunity: String){
        if(idCommunity.isNotBlank() && community == null)
            community = idCommunity
    }

    fun startThread(content: ThreadData, id: String, userName: String, userId: String){ // Updates the creator to the logged user
            creator = userId
            footer = userName
            header = content.title
            body = content.body
            this.id = id
    }

    fun like(userEmail: String){
        likes.add(userEmail)
    }
    fun removeLike(userEmail: String){
        likes.remove(userEmail)
    }
    fun removeLikeSubThread( emailUser: String, idSubThread: String){
        if(idSubThread in subThreadId){
            val indexSubThread = getSubThreadIndex(idSubThread)
            subThread[indexSubThread].likes.remove(emailUser)
        }
    }

    fun dislike(userEmail: String){
        dislikes.add(userEmail)
    }

    fun removeDislike(userEmail: String){
        dislikes.remove(userEmail)
    }

    fun removeDislikeSubThread( emailUser: String, idSubThread: String){
        if(idSubThread in subThreadId){
            val indexSubThread = getSubThreadIndex(idSubThread)
            subThread[indexSubThread].dislikes.remove(emailUser)
        }
    }

    fun likeToDislike(userId: String){
        if(userId in likes){
            likes.remove(userId)
            dislikes.add(userId)
        }
    }

    fun dislikeToLike(userId: String) {
        if(userId in dislikes){
            dislikes.remove(userId)
            likes.add(userId)
        }
    }

    fun likeSubThread(idUser: String, idSubThread: String){

        if(idSubThread in subThreadId){
            val indexSubThread = getSubThreadIndex(idSubThread)
            subThread[indexSubThread].likes.add(idUser)
        }
    }

    fun dislikeSubThread(idUser: String, idSubThread: String){

        if(idSubThread in subThreadId){
            val indexSubThread = getSubThreadIndex(idSubThread)
            subThread[indexSubThread].dislikes.add(idUser)
        }
    }

    fun likeToDislikeSubThread(idUser: String, idSubThread: String){
        if(idSubThread in subThreadId){
            val indexSubThread = getSubThreadIndex(idSubThread)
            subThread[indexSubThread].likes.remove(idUser)
            subThread[indexSubThread].dislikes.add(idUser)
        }
    }

    fun dislikeToLikeSubThread(idUser: String, idSubThread: String) {
        if(idSubThread in subThreadId){
            val indexSubThread = getSubThreadIndex(idSubThread)

            subThread[indexSubThread].dislikes.remove(idUser)
            subThread[indexSubThread].likes.add(idUser)
        }
    }

    fun addSubThread(sub: SubThread){
        if(sub.creator != ""){
            subThreadId.add(sub.id)
            subThread.add(sub)
        }
    }

    fun removeSubThread(idSubThread: String, idCreator: String){

        val indexSubThread = getSubThreadIndex(idSubThread)
        if(idSubThread in subThreadId && idCreator == subThread[indexSubThread].creator){

            subThreadId.remove(subThread[indexSubThread].id)
            subThread.remove(subThread[indexSubThread])
        }
    }

    fun getSubThreadById(idSubThread: String): SubThread {
        val nullSubThread = SubThread(mutableListOf(), mutableListOf(), "", "", "", "", "")
        return if(idSubThread in subThreadId){
            val indexSubThread = getSubThreadIndex(idSubThread)
            subThread[indexSubThread]
        }
        else nullSubThread
    }
    //SETTERS

    //GETTERS
    private fun getSubThreadIndex(id: String): Int {

        if(id in subThreadId){
            for(i in 0 until subThread.size){
                if (subThread[i].id == id)
                    return i
            }
            return -1
        }
        else return -1
    }

    fun getSubThreadCreator(idSubThread: String): String {
        val indexSubThread = getSubThreadIndex(idSubThread)
        return if(indexSubThread != -1)
            subThread[indexSubThread].creator
        else
            ""
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