package br.meetingplace.management.interfaces.thread

import br.meetingplace.data.threads.subThread.SubThreadContent
import br.meetingplace.data.threads.subThread.SubThreadOperations
import br.meetingplace.management.interfaces.file.DeleteFile
import br.meetingplace.management.interfaces.file.ReadFile
import br.meetingplace.management.interfaces.file.WriteFile
import br.meetingplace.management.interfaces.utility.Generator
import br.meetingplace.management.interfaces.utility.Path
import br.meetingplace.management.interfaces.utility.Refresh
import br.meetingplace.management.interfaces.utility.Verifiers
import br.meetingplace.services.thread.SubThread
import br.meetingplace.services.notification.Inbox

interface SubThread: ReadFile, WriteFile, DeleteFile, Refresh, Generator, Path, Verifiers {

    fun createSubThread(subThreadData: SubThreadContent){
        val management = readLoggedUser().email
        if(verifyPath("users",management) && verifyPath("threads",subThreadData.idThread)
                && management != "" && verifyUserSocialProfile()){

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
                && management != "" && verifyUserSocialProfile()){

            val thread = readThread(dislike.idMainThread)
            val subThread = thread.getSubThreadById(dislike.idSubThread)
            val checker = ThreadChecker.getChecker()
            when (checker.checkLikeDislike(subThread)) {
                0 -> {
                    thread.likeToDislikeSubThread(management,dislike.idSubThread)
                    writeThread(thread.getId(),thread)
                } // like to dislike
                1->{
                    thread.removeDislikeSubThread(management,dislike.idSubThread)
                    writeThread(thread.getId(),thread)
                }
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
                && management != "" && verifyUserSocialProfile()){
            val user = readUser(management)

            val thread = readThread(like.idMainThread)
            val subThread = thread.getSubThreadById(like.idSubThread)
            val notification = Inbox("${user.social.getUserName()} liked your reply.", "Thread.")

            if(verifyPath("users",subThread.creator)){
                val userCreator = readUser(subThread.creator)
                val checker = ThreadChecker.getChecker()
                when (checker.checkLikeDislike(subThread)) {
                    0->{
                        thread.removeLikeSubThread(management,like.idSubThread)
                        writeThread(thread.getId(),thread)
                    }
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
                && management != "" && verifyUserSocialProfile()){

            val thread = readThread(operations.idMainThread)

            thread.removeSubThread(operations.idSubThread,management)
            writeThread(thread.getId(), thread)
        }
    }//DELETE
}