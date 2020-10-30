package br.meetingplace.management.services.thread.dependencies.mainThread

import br.meetingplace.data.threads.ThreadOperationsData
import br.meetingplace.management.dependencies.Verify
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteCommunity
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteThread
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteUser
import br.meetingplace.management.services.thread.dependencies.LikeInterface
import br.meetingplace.services.entitie.User
import br.meetingplace.services.notification.Inbox
import br.meetingplace.services.thread.MainThread

class LikeMainThread private constructor(): LikeInterface, ReadWriteCommunity, ReadWriteUser, ReadWriteLoggedUser, ReadWriteThread, Verify {

    companion object{
        private val Class = LikeMainThread()
        fun getLikeOperator() = Class
    }

    override fun like(data: ThreadOperationsData){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val thread = readThread(data.idThread)
        lateinit var notification: Inbox
        lateinit var creator: User

        if(verifyThread(thread) && verifyLoggedUser(user) && data.idSubThread == null) {

            notification = Inbox("${user.getUserName()} liked your thread.", "Thread.")
            creator = readUser(thread.getCreator())

            when (checkLikeDislike(thread)) {
                0->{
                    thread.removeLike(loggedUser)
                    writeThread(thread,thread.getId())
                }
                1-> {// DISLIKED to LIKED
                    if(thread.getCreator() != loggedUser){
                        creator.updateInbox(notification)
                        writeUser(creator, creator.getEmail())
                    }
                    creator.updateInbox(notification)
                    thread.dislikeToLike(loggedUser)
                    writeThread(thread,thread.getId())
                }
                2 -> {// 2 hasn't liked yet
                    if(thread.getCreator() != loggedUser){
                        creator.updateInbox(notification)
                        writeUser(creator, creator.getEmail())
                    }
                    thread.like(loggedUser)
                    writeThread(thread,thread.getId())
                }
            }

        }
    }//UPDATE

    override fun dislike(data: ThreadOperationsData) {
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val thread = readThread(data.idThread)

        if(verifyLoggedUser(user) && verifyThread(thread) && data.idSubThread == null) {

            when (checkLikeDislike(thread)) {
                0 -> {
                    thread.likeToDislike(loggedUser)
                    writeThread(thread,thread.getId())
                } // like to dislike
                1->{
                    thread.removeDislike(loggedUser)
                    writeThread(thread,thread.getId())
                }
                2 -> {
                    thread.dislike(loggedUser)
                    writeThread(thread,thread.getId())
                } // hasn't DISLIKED yet
            }
        }
    } //UPDATE

    private fun checkLikeDislike(thread: MainThread): Int {// IF TRUE THE USER ALREADY LIKED OR DISLIKED THE THREAD
        val log = readLoggedUser()
        return when (log.email) {
            in thread.getLikes() // 0 ALREADY LIKED
            -> 0
            in thread.getDislikes() // 1 ALREADY DISLIKED
            -> 1
            else -> 2 // 2 hasn't DISLIKED or liked yet
        }
    }

}