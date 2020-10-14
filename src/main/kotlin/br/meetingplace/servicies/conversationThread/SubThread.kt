package br.meetingplace.servicies.conversationThread

import br.meetingplace.data.threads.ThreadContent

class SubThread: MainThread(){

    //Only accessible via the MainThread
    override fun removeSubThread(idSubThread: Int, idCreator: Int){}
    override fun addSubThread(sub: SubThread){}
    override fun likeSubThread(idUser: Int, idSubThread: Int){}
    override fun dislikeSubThread(idUser: Int, idSubThread: Int){}
    override fun likeToDislikeSubThread(idUser: Int, idSubThread: Int){}
    override fun dislikeToLikeSubThread(idUser: Int, idSubThread: Int) {}
    //Only accessible via the MainThread
}