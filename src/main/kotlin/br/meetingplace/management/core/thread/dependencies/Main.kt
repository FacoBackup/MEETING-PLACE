package br.meetingplace.management.core.thread.dependencies

import br.meetingplace.data.threads.ThreadData
import br.meetingplace.data.threads.ThreadOperationsData
import br.meetingplace.management.core.operators.Generator
import br.meetingplace.management.core.operators.Verify
import br.meetingplace.management.core.operators.fileOperators.DeleteFile
import br.meetingplace.management.core.operators.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.management.core.operators.fileOperators.rw.ReadWriteThread
import br.meetingplace.management.core.operators.fileOperators.rw.ReadWriteUser
import br.meetingplace.services.notification.Inbox
import br.meetingplace.services.thread.MainThread

class Main private constructor(): ThreadInterface, Verify, ReadWriteLoggedUser, ReadWriteUser, ReadWriteThread, Generator{

    companion object{
        private val op = Main()
        fun getMainThreadOperator() = op
    }

    override fun create(data: ThreadData): String?{
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        if(verifyLoggedUser(user)){
            val thread = MainThread()
            thread.startThread(data,generateId(), user.social.getUserName(), loggedUser)
            writeThread(thread, thread.getId())
            user.social.updateMyThreads(thread.getId(),true)
            writeUser(user, user.getEmail())
            return if(!data.idCommunity.isNullOrBlank())
                thread.getId()
            else null
        }
        return null
    } //CREATE

    override fun like(data: ThreadOperationsData){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val thread = readThread(data.idThread)

        if(verifyThread(thread) && verifyLoggedUser(user) && data.idSubThread == null) {

            val notification = Inbox("${user.social.getUserName()} liked your thread.", "Thread.")
            val userCreator = readUser(thread.getCreator())

            when (ThreadChecker.getChecker().checkLikeDislike(thread)) {
                0->{
                    thread.removeLike(loggedUser)
                    writeThread(thread,thread.getId())
                }
                1-> {// DISLIKED to LIKED
                    if(thread.getCreator() != loggedUser){
                        userCreator.social.updateInbox(notification)
                        writeUser(userCreator, userCreator.getEmail())
                    }
                    userCreator.social.updateInbox(notification)
                    thread.dislikeToLike(loggedUser)
                    writeThread(thread,thread.getId())
                }
                2 -> {// 2 hasn't liked yet
                    if(thread.getCreator() != loggedUser){
                        userCreator.social.updateInbox(notification)
                        writeUser(userCreator, userCreator.getEmail())
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

            val checker = ThreadChecker.getChecker()
            when (checker.checkLikeDislike(thread)) {
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

    override fun delete(data: ThreadOperationsData){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val thread = readThread(data.idThread)

        if(verifyThread(thread) && verifyLoggedUser(user) && data.idSubThread == null ) {

            DeleteFile.getDeleteFileOperator().deleteThread(thread)
            user.social.updateMyThreads(data.idThread,false) //FALSE IS TO REMOVE THREAD
            writeUser(user, user.getEmail())
        }
    }//DELETE

    fun deleteAllThreadsFromUserId(){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)

        if(verifyLoggedUser(user)){
            val myThreads = user.social.getMyThreads()
            for(i in 0 until myThreads.size){
                val thread = readThread(myThreads[i])
                if(verifyThread(thread))
                    DeleteFile.getDeleteFileOperator().deleteThread(thread)
            }
        }
    } //DELETE
}