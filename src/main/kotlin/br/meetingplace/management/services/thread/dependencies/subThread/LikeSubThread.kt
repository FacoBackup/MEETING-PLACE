package br.meetingplace.management.services.thread.dependencies.subThread

import br.meetingplace.data.threads.ThreadOperationsData
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.readwrite.controller.ReadWriteController
import br.meetingplace.management.dependencies.verify.controller.VerifyController
import br.meetingplace.management.services.thread.dependencies.LikeInterface
import br.meetingplace.services.entitie.User
import br.meetingplace.services.notification.Inbox
import br.meetingplace.services.thread.SubThread

class LikeSubThread private constructor(): LikeInterface{
    private val iDs = IDsController.getClass()
    private val rw = ReadWriteController.getClass()
    private val verify = VerifyController.getClass()

    companion object{
        private val Class = LikeSubThread()
        fun getLikeOperator() = Class
    }

    override fun like(data: ThreadOperationsData){
        val loggedUser = rw.readLoggedUser().email
        val thread = rw.readThread(data.idThread)
        val user = rw.readUser(loggedUser)
        lateinit var notification: Inbox
        lateinit var subThread: SubThread
        lateinit var creator: User

        if(verify.verifyUser(user) && verify.verifyThread(thread) && data.idSubThread != null){

            subThread = thread.getSubThreadById(data.idSubThread)
            notification = Inbox("${user.getUserName()} liked your reply.", "Thread.")
            creator = rw.readUser(subThread.creator)

            if(verify.verifyUser(creator)){
                when (checkLikeDislike(subThread)) {
                    0->{
                        thread.removeLikeSubThread(loggedUser,data.idSubThread)
                        rw.writeThread(thread,thread.getId())
                    }
                    1 -> {
                        if(user.getEmail() != creator.getEmail()){
                            creator.updateInbox(notification)
                            rw.writeUserToFile(creator,iDs.attachNameToEmail(creator.getUserName(),creator.getEmail()))
                        }
                        thread.dislikeToLikeSubThread(loggedUser,data.idSubThread)
                        rw.writeThread(thread,thread.getId())
                    } // like to dislike
                    2 -> {
                        if(user.getEmail() != creator.getEmail()){
                            creator.updateInbox(notification)
                            rw.writeUserToFile(creator,iDs.attachNameToEmail(creator.getUserName(),creator.getEmail()))
                        }
                        thread.likeSubThread(loggedUser,data.idSubThread)
                        rw.writeThread(thread,thread.getId())
                    } // hasn't DISLIKED yet
                }
            }
        }
    }//UPDATE


    override fun dislike(data: ThreadOperationsData) {
        val loggedUser = rw.readLoggedUser().email
        val thread = rw.readThread(data.idThread)
        val user = rw.readUser(loggedUser)
        lateinit var subThread: SubThread
        if(verify.verifyUser(user) && data.idSubThread != null && verify.verifyThread(thread)){
            subThread = thread.getSubThreadById(data.idSubThread)

            when (checkLikeDislike(subThread)) {
                0 -> {
                    thread.likeToDislikeSubThread(loggedUser,data.idSubThread)
                    rw.writeThread(thread,thread.getId())
                } // like to dislike
                1->{
                    thread.removeDislikeSubThread(loggedUser,data.idSubThread)
                    rw.writeThread(thread,thread.getId())
                }
                2 -> {
                    thread.dislikeSubThread(loggedUser,data.idSubThread)
                    rw.writeThread(thread,thread.getId())
                } // hasn't DISLIKED yet
            }
        }
    }//UPDATE


    private fun checkLikeDislike(thread: SubThread): Int { //CHECKER
        val log = rw.readLoggedUser()
        val management = log.email
        return if(thread.id != ""){
            when (management) {
                in thread.likes // 0 ALREADY LIKED
                -> 0
                in thread.dislikes// 1 ALREADY DISLIKED
                -> 1
                else -> 2 // 2 hasn't DISLIKED or liked yet
            }
        }
        else 3 // SubThread Doesn't exist
    }
}