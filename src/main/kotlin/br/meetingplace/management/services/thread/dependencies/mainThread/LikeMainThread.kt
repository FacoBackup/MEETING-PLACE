package br.meetingplace.management.services.thread.dependencies.mainThread

import br.meetingplace.data.threads.ThreadOperationsData
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.readwrite.controller.ReadWriteController
import br.meetingplace.management.dependencies.verify.controller.VerifyController
import br.meetingplace.management.services.thread.dependencies.LikeInterface
import br.meetingplace.services.entitie.User
import br.meetingplace.services.notification.Inbox
import br.meetingplace.services.thread.MainThread

class LikeMainThread private constructor(): LikeInterface{
    private val iDs = IDsController.getClass()
    private val rw = ReadWriteController.getClass()
    private val verify = VerifyController.getClass()

    companion object{
        private val Class = LikeMainThread()
        fun getLikeOperator() = Class
    }

    override fun like(data: ThreadOperationsData){
        val loggedUser = rw.readLoggedUser().email
        val user =  rw.readUser(loggedUser)
        val thread =  rw.readThread(data.idThread)
        lateinit var notification: Inbox
        lateinit var creator: User

        if(verify.verifyThread(thread) && verify.verifyUser(user) && data.idSubThread == null) {

            notification = Inbox("${user.getUserName()} liked your thread.", "Thread.")
            creator =  rw.readUser(thread.getCreator())

            when (checkLikeDislike(thread)) {
                0->{
                    thread.removeLike(loggedUser)
                    rw.writeThread(thread,thread.getId())
                }
                1-> {// DISLIKED to LIKED
                    if(thread.getCreator() != loggedUser){
                        creator.updateInbox(notification)
                        rw.writeUserToFile(creator,iDs.attachNameToEmail(creator.getUserName(),creator.getEmail()))
                    }
                    creator.updateInbox(notification)
                    thread.dislikeToLike(loggedUser)
                    rw.writeThread(thread,thread.getId())
                }
                2 -> {// 2 hasn't liked yet
                    if(thread.getCreator() != loggedUser){
                        creator.updateInbox(notification)
                        rw.writeUserToFile(creator,iDs.attachNameToEmail(creator.getUserName(),creator.getEmail()))
                    }
                    thread.like(loggedUser)
                    rw.writeThread(thread,thread.getId())
                }
            }

        }
    }//UPDATE

    override fun dislike(data: ThreadOperationsData) {
        val loggedUser = rw.readLoggedUser().email
        val user =  rw.readUser(loggedUser)
        val thread =  rw.readThread(data.idThread)

        if(verify.verifyUser(user) && verify.verifyThread(thread) && data.idSubThread == null) {

            when (checkLikeDislike(thread)) {
                0 -> {
                    thread.likeToDislike(loggedUser)
                    rw.writeThread(thread,thread.getId())
                } // like to dislike
                1->{
                    thread.removeDislike(loggedUser)
                    rw.writeThread(thread,thread.getId())
                }
                2 -> {
                    thread.dislike(loggedUser)
                    rw.writeThread(thread,thread.getId())
                } // hasn't DISLIKED yet
            }
        }
    } //UPDATE

    private fun checkLikeDislike(thread: MainThread): Int {// IF TRUE THE USER ALREADY LIKED OR DISLIKED THE THREAD
        val log =  rw.readLoggedUser()
        return when (log.email) {
            in thread.getLikes() // 0 ALREADY LIKED
            -> 0
            in thread.getDislikes() // 1 ALREADY DISLIKED
            -> 1
            else -> 2 // 2 hasn't DISLIKED or liked yet
        }
    }

}