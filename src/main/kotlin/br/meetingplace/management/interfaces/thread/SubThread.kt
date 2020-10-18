package br.meetingplace.management.interfaces.thread

import br.meetingplace.data.threads.subThread.SubThreadContent
import br.meetingplace.data.threads.subThread.SubThreadOperations
import br.meetingplace.interfaces.file.DeleteFile
import br.meetingplace.interfaces.file.ReadFile
import br.meetingplace.interfaces.file.WriteFile
import br.meetingplace.interfaces.utility.Generator
import br.meetingplace.interfaces.utility.Path
import br.meetingplace.interfaces.utility.Refresh
import br.meetingplace.interfaces.utility.Verifiers
import br.meetingplace.servicies.conversationThread.SubThread
import br.meetingplace.servicies.notification.Inbox

interface SubThread: ReadFile, WriteFile, DeleteFile, Refresh, Generator, Path, Verifiers {

    fun createSubThread(subThreadData: SubThreadContent){
        val management = readLoggedUser().email
        if(verifyPath("users",management) && verifyPath("threads",subThreadData.idThread)
                && management != "" && verifyUserSocialProfile(management)){

            val user = readUser(management)
            val thread = readThread(subThreadData.idThread)

            val subThread = SubThread(mutableListOf(),mutableListOf(), management, subThreadData.title, subThreadData.body, user.social.getUserName(), generateId())

            thread.addSubThread(subThread)
            writeThread(thread.getId(), thread)
        }
    }// CREATE
    fun dislikeSubThread(dislike: SubThreadOperations) {
        val management = readLoggedUser().email

        if(verifyPath("users",management) && verifyPath("threads",dislike.idMainThread)
                && management != "" && verifyUserSocialProfile(management)){

            val thread = readThread(dislike.idMainThread)
            val subThread = thread.getSubThreadById(dislike.idSubThread)
            val checker = ThreadChecker.getChecker()
            when (checker.checkLikeDislike(subThread)) {
                0 -> {
                    thread.likeToDislikeSubThread(management,dislike.idSubThread)
                    writeThread(thread.getId(),thread)
                } // like to dislike
                2 -> {
                    thread.dislikeSubThread(management,dislike.idSubThread)
                    writeThread(thread.getId(),thread)
                } // hasn't DISLIKED yet
            }
        }
    }//UPDATE
    fun likeSubThread(like: SubThreadOperations){
        val management = readLoggedUser().email
        if(verifyPath("users",management) && verifyPath("threads",like.idMainThread)
                && management != "" && verifyUserSocialProfile(management)){
            val user = readUser(management)

            val thread = readThread(like.idMainThread)
            val subThread = thread.getSubThreadById(like.idSubThread)
            val notification = Inbox("${user.social.getUserName()} liked your reply.", "Thread.")

            if(verifyPath("users",subThread.creator)){
                val userCreator = readUser(subThread.creator)
                val checker = ThreadChecker.getChecker()
                when (checker.checkLikeDislike(subThread)) {
                    1 -> {
                        if(user.getEmail() != userCreator.getEmail()){
                            userCreator.social.updateInbox(notification)
                            writeUser(userCreator.getEmail(), userCreator)
                        }
                        thread.dislikeToLikeSubThread(management,like.idSubThread)
                        writeThread(thread.getId(),thread)
                    } // like to dislike
                    2 -> {
                        if(user.getEmail() != userCreator.getEmail()){
                            userCreator.social.updateInbox(notification)
                            writeUser(userCreator.getEmail(), userCreator)
                        }
                        thread.likeSubThread(management,like.idSubThread)
                        writeThread(thread.getId(),thread)
                    } // hasn't DISLIKED yet
                }
            }
        }
    }//UPDATE
    fun deleteSubThread(operations: SubThreadOperations){
        val management = readLoggedUser().email

        if(verifyPath("users",management) && verifyPath("threads",operations.idMainThread)
                && management != "" && verifyUserSocialProfile(management)){

            val user = readUser(management)
            val thread = readThread(operations.idMainThread)

            thread.removeSubThread(operations.idSubThread,management)
            writeThread(thread.getId(), thread)
        }
    }//DELETE
}