package br.meetingplace.management.services.thread.dependencies.subThread

import br.meetingplace.data.threads.ThreadOperationsData
import br.meetingplace.management.dependencies.Verify
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteCommunity
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteThread
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteUser
import br.meetingplace.management.services.thread.dependencies.LikeInterface
import br.meetingplace.services.entitie.User
import br.meetingplace.services.notification.Inbox
import br.meetingplace.services.thread.SubThread

class LikeSubThread private constructor(): LikeInterface, ReadWriteCommunity, ReadWriteUser, ReadWriteLoggedUser, ReadWriteThread, Verify{

    companion object{
        private val Class = LikeSubThread()
        fun getLikeOperator() = Class
    }

    override fun like(data: ThreadOperationsData){
        val loggedUser = readLoggedUser().email
        val thread = readThread(data.idThread)
        val user = readUser(loggedUser)
        lateinit var notification: Inbox
        lateinit var subThread: SubThread
        lateinit var creator: User

        if(verifyLoggedUser(user) && verifyThread(thread) && data.idSubThread != null){

            subThread = thread.getSubThreadById(data.idSubThread)
            notification = Inbox("${user.getUserName()} liked your reply.", "Thread.")
            creator = readUser(subThread.creator)

            if(verifyUser(creator)){
                when (checkLikeDislike(subThread)) {
                    0->{
                        thread.removeLikeSubThread(loggedUser,data.idSubThread)
                        writeThread(thread,thread.getId())
                    }
                    1 -> {
                        if(user.getEmail() != creator.getEmail()){
                            creator.updateInbox(notification)
                            writeUser(creator, creator.getEmail())
                        }
                        thread.dislikeToLikeSubThread(loggedUser,data.idSubThread)
                        writeThread(thread,thread.getId())
                    } // like to dislike
                    2 -> {
                        if(user.getEmail() != creator.getEmail()){
                            creator.updateInbox(notification)
                            writeUser(creator, creator.getEmail())
                        }
                        thread.likeSubThread(loggedUser,data.idSubThread)
                        writeThread(thread,thread.getId())
                    } // hasn't DISLIKED yet
                }
            }
        }
    }//UPDATE


    override fun dislike(data: ThreadOperationsData) {
        val loggedUser = readLoggedUser().email
        val thread = readThread(data.idThread)
        val user = readUser(loggedUser)
        lateinit var subThread: SubThread
        if(verifyLoggedUser(user) && data.idSubThread != null && verifyThread(thread)){
            subThread = thread.getSubThreadById(data.idSubThread)

            when (checkLikeDislike(subThread)) {
                0 -> {
                    thread.likeToDislikeSubThread(loggedUser,data.idSubThread)
                    writeThread(thread,thread.getId())
                } // like to dislike
                1->{
                    thread.removeDislikeSubThread(loggedUser,data.idSubThread)
                    writeThread(thread,thread.getId())
                }
                2 -> {
                    thread.dislikeSubThread(loggedUser,data.idSubThread)
                    writeThread(thread,thread.getId())
                } // hasn't DISLIKED yet
            }
        }
    }//UPDATE


    private fun checkLikeDislike(thread: SubThread): Int { //CHECKER
        val log = readLoggedUser()
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