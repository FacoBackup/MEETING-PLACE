package br.meetingplace.management.interfaces.thread

import br.meetingplace.data.threads.mainThread.ThreadContent
import br.meetingplace.data.threads.mainThread.ThreadOperations
import br.meetingplace.management.interfaces.file.DeleteFile
import br.meetingplace.management.interfaces.file.ReadFile
import br.meetingplace.management.interfaces.file.WriteFile
import br.meetingplace.management.interfaces.utility.Generator
import br.meetingplace.management.interfaces.utility.Path
import br.meetingplace.management.interfaces.utility.Refresh
import br.meetingplace.management.interfaces.utility.Verifiers
import br.meetingplace.servicies.thread.MainThread
import br.meetingplace.servicies.notification.Inbox
import java.io.File

interface MainThread: ReadFile, WriteFile, DeleteFile, Refresh, Generator, Path, Verifiers {

    fun createMainThread(content: ThreadContent){
        val management = readLoggedUser().email

        if(verifyPath("users",management) && management != "" && verifyUserSocialProfile()){
            val thread = MainThread()
            val user = readUser(management)

            thread.startThread(content,generateId(), user.social.getUserName(), management)
            writeThread(thread.getId(), thread)
            user.social.updateMyThreads(thread.getId(),true)
            writeUser(user.getEmail(),user)
        }
    } //CREATE
    fun getMyThreads(): MutableList<MainThread> {
        val management = readLoggedUser().email
        val myThreads = mutableListOf<MainThread>()

        if(verifyPath("users",management) && management != ""){
            val user = readUser(management)
            val myThreadsIds = user.social.getMyThreads()

            for (i in 0 until myThreadsIds.size){
                if (verifyPath("threads",myThreadsIds[i])){
                    val thread = readThread(myThreadsIds[i])
                    myThreads.add(thread)
                }
            }
            return myThreads
        }
        return myThreads
    }//READ
    fun getMyTimeline(): MutableList<MainThread> {
        val management = readLoggedUser().email

        val myTimeline = mutableListOf<MainThread>()

        if( verifyPath("users",management) && management != "") {
            val user = readUser(management)
            val followingIds = user.social.getFollowing()

            for (i in 0 until followingIds.size){
                if( verifyPath("users",followingIds[i])){

                    val following = readUser(followingIds[i])
                    val followingThreads = following.social.getMyThreads()

                    for (j in 0 until followingThreads.size){
                        if (verifyPath("threads",followingThreads[j])){
                            val thread = readThread(followingThreads[j])
                            myTimeline.add(thread)
                        }
                    }
                }
            }
            return myTimeline
        }
        return myTimeline
    }//READ
    fun likeThread(like: ThreadOperations){
        val management = readLoggedUser().email

        if(verifyPath("users",management) && verifyPath("threads",like.idThread)
                && management != "" && verifyUserSocialProfile()) {
            val user = readUser(management)
            val thread = readThread(like.idThread)
            val notification = Inbox("${user.social.getUserName()} liked your thread.", "Thread.")


            if(verifyPath("users",thread.getCreator())){
                val userCreator = readUser(thread.getCreator())
                val checker = ThreadChecker.getChecker()

                when (checker.checkLikeDislike(thread)) {
                    0->{
                        thread.removeLike(management)
                        writeThread(thread.getId(),thread)
                    }
                    1-> {// DISLIKED to LIKED
                        if(thread.getCreator() != management){
                            userCreator.social.updateInbox(notification)
                            writeUser(thread.getCreator(),userCreator)
                        }
                        userCreator.social.updateInbox(notification)
                        thread.dislikeToLike(management)
                        writeThread(thread.getId(),thread)
                    }
                    2 -> {// 2 hasn't liked yet
                        if(thread.getCreator() != management){
                            userCreator.social.updateInbox(notification)
                            writeUser(thread.getCreator(),userCreator)
                        }
                        thread.like(management)
                        writeThread(thread.getId(),thread)
                    }
                }
            }
        }
    }//UPDATE
    fun dislikeThread(dislike: ThreadOperations) {
        val management = readLoggedUser().email
        if(verifyPath("users",management) && verifyPath("threads",dislike.idThread)
                && management != "" && verifyUserSocialProfile()) {

            val thread = readThread(dislike.idThread)
            val checker = ThreadChecker.getChecker()
            when (checker.checkLikeDislike(thread)) {
                0 -> {
                    thread.likeToDislike(management)
                    writeThread(thread.getId(),thread)
                } // like to dislike
                1->{
                    thread.removeDislike(management)
                    writeThread(thread.getId(),thread)
                }
                2 -> {
                    thread.dislike(management)
                    writeThread(thread.getId(),thread)
                } // hasn't DISLIKED yet
            }
        }
    } //UPDATE
    fun deleteThread(operations: ThreadOperations){
        val management = readLoggedUser().email

        if(verifyPath("users",management) && verifyPath("threads",operations.idThread)
                && management != "" && verifyUserSocialProfile()){

            val user = readUser(management)
            delete(File("${operations.idThread}.json"))
            user.social.updateMyThreads(operations.idThread,false) //FALSE IS TO REMOVE THREAD
            writeUser(user.getEmail(), user)
        }
    }//DELETE
    fun deleteAllThreadsFromUserId(){
        val management = readLoggedUser().email

        if(verifyPath("user",management) && management != "" && verifyUserSocialProfile()){

            val user = readUser(management)
            val threads = user.social.getMyThreads()

            for(i in 0 until threads.size){

                if(verifyPath("threads",threads[i]))
                    delete(File("${threads[i]}.json")) //HERE
            }
        }
    } //DELETE
}