package br.meetingplace.management.core.thread.dependencies

import br.meetingplace.data.threads.ThreadData
import br.meetingplace.data.threads.ThreadOperationsData
import br.meetingplace.management.core.operators.Generator
import br.meetingplace.management.core.operators.Verify
import br.meetingplace.management.core.operators.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.management.core.operators.fileOperators.rw.ReadWriteThread
import br.meetingplace.management.core.operators.fileOperators.rw.ReadWriteUser
import br.meetingplace.services.notification.Inbox
import br.meetingplace.services.thread.SubThread

class Sub private constructor(): ThreadInterface, Verify, ReadWriteLoggedUser, ReadWriteUser, ReadWriteThread, Generator {

    companion object{
        private val op = Sub()
        fun getSubThreadOperator() = op
    }

    override fun create(data: ThreadData, type: ThreadType){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)

        if(verifyLoggedUser(user) && type == ThreadType.SUB){
            val thread = readThread(data.idThread!!)
            if(thread.getId() != ""){
                val subThread = SubThread(mutableListOf(),mutableListOf(), loggedUser, data.title, data.body, user.social.getUserName(), generateId())
                thread.addSubThread(subThread)
                writeThread(thread, thread.getId())
            }
        }
    }// CREATE

    override fun dislike(data: ThreadOperationsData, type: ThreadType) {
        val loggedUser = readLoggedUser().email
        val thread = readThread(data.idThread)
        val user = readUser(loggedUser)

        if(verifyLoggedUser(user) && data.idSubThread != null && verifyThread(thread) && type == ThreadType.SUB){
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

    override fun like(data: ThreadOperationsData, type: ThreadType){
        val loggedUser = readLoggedUser().email
        val thread = readThread(data.idThread)
        val user = readUser(loggedUser)

        if(verifyLoggedUser(user) && verifyThread(thread) && data.idSubThread != null && type == ThreadType.SUB){

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
    override fun delete(data: ThreadOperationsData, type: ThreadType){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val thread = readThread(data.idThread)
        if(verifyLoggedUser(user) && thread.getId() != "" && data.idSubThread != null && type == ThreadType.SUB){

            thread.removeSubThread(data.idSubThread,loggedUser)
            writeThread(thread,thread.getId())
        }
    }//DELETE
}