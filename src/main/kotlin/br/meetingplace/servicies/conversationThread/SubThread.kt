package br.meetingplace.servicies.conversationThread

class SubThread: MainThread(){
    override fun getLikes() = mutableListOf<Int>()
    override fun getDislikes() = mutableListOf<Int>()

    override fun getLikeSize() = -1
    override fun getDislikeSize() = -1

}