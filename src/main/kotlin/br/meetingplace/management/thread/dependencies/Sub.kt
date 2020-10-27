package br.meetingplace.management.thread.dependencies

import br.meetingplace.data.threads.ThreadData
import br.meetingplace.data.threads.ThreadOperationsData
import br.meetingplace.management.dependencies.Generator
import br.meetingplace.management.dependencies.Verify
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteThread
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteUser
import br.meetingplace.services.notification.Inbox
import br.meetingplace.services.thread.SubThread

class Sub private constructor(): ThreadInterface, Verify, ReadWriteLoggedUser, ReadWriteUser, ReadWriteThread, Generator {

    companion object{
        private val op = Sub()
        fun getSubThreadOperator() = op
    }

    override fun create(data: ThreadData): String?{
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)

        if(verifyLoggedUser(user)){
            val thread = readThread(data.idThread!!)
            if(thread.getId() != ""){
                val subThread = SubThread(mutableListOf(),mutableListOf(), loggedUser, data.title, data.body, user.social.getUserName(), generateId())
                thread.addSubThread(subThread)
                writeThread(thread, thread.getId())
            }
        }
        return null
    }// CREATE

    override fun dislike(data: ThreadOperationsData) {
        val loggedUser = readLoggedUser().email
        val thread = readThread(data.idThread)
        val user = readUser(loggedUser)

        if(verifyLoggedUser(user) && data.idSubThread != null && verifyThread(thread)){
            val subThread = thread.getSubThreadById(data.idSubThread)

            when (ThreadChecker.getChecker().checkLikeDislike(subThread)) {
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

    override fun like(data: ThreadOperationsData){
        val loggedUser = readLoggedUser().email
        val thread = readThread(data.idThread)
        val user = readUser(loggedUser)

        if(verifyLoggedUser(user) && verifyThread(thread) && data.idSubThread != null){

            val subThread = thread.getSubThreadById(data.idSubThread)
            val notification = Inbox("${user.social.getUserName()} liked your reply.", "Thread.")
            val userCreator = readUser(subThread.creator)

            if(verifyUser(userCreator)){
                when (ThreadChecker.getChecker().checkLikeDislike(subThread)) {
                    0->{
                        thread.removeLikeSubThread(loggedUser,data.idSubThread)
                        writeThread(thread,thread.getId())
                    }
                    1 -> {
                        if(user.getEmail() != userCreator.getEmail()){
                            userCreator.social.updateInbox(notification)
                            writeUser(userCreator, userCreator.getEmail())
                        }
                        thread.dislikeToLikeSubThread(loggedUser,data.idSubThread)
                        writeThread(thread,thread.getId())
                    } // like to dislike
                    2 -> {
                        if(user.getEmail() != userCreator.getEmail()){
                            userCreator.social.updateInbox(notification)
                            writeUser(userCreator, userCreator.getEmail())
                        }
                        thread.likeSubThread(loggedUser,data.idSubThread)
                        writeThread(thread,thread.getId())
                    } // hasn't DISLIKED yet
                }
            }
        }
    }//UPDATE
    override fun delete(data: ThreadOperationsData){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val thread = readThread(data.idThread)
        if(verifyLoggedUser(user) && thread.getId() != "" && data.idSubThread != null){

            thread.removeSubThread(data.idSubThread,loggedUser)
            writeThread(thread,thread.getId())
        }
    }//DELETE
}