package br.meetingplace.management.servicies

import br.meetingplace.data.threads.SubThreadContent
import br.meetingplace.data.threads.operations.SubThreadOperations
import br.meetingplace.data.threads.ThreadContent
import br.meetingplace.data.threads.operations.ThreadOperations
import br.meetingplace.interfaces.*
import br.meetingplace.servicies.conversationThread.MainThread
import br.meetingplace.servicies.conversationThread.SubThread
import br.meetingplace.servicies.notification.Inbox

import java.io.File


class ThreadManagement private constructor(): ReadFile, WriteFile,DeleteFile,Refresh,Generator, Path, Verifiers{

    companion object{
        private val threadManagement = ThreadManagement()
        fun getManagement() = threadManagement
    }

    fun deleteAllThreadsFromUserId(){
        val log = refreshData()
        val management = log.user

        if(verifyPath("user",management) && management != "" && verifyUserSocialProfile(management)){

            val user = readUser(management)
            val threads = user.social.getMyThreads()

            for(i in 0 until threads.size){

                if(verifyPath("threads",threads[i]))
                    delete(File("${threads[i]}.json")) //HERE
            }
        }
    }

    //MAINTHREAD CRUD OPERATIONS
    fun createMainThread(content: ThreadContent){
        val log = refreshData()
        val management = log.user

        if(verifyPath("users",management) && management != "" && verifyUserSocialProfile(management)){
            val thread = MainThread()
            val user = readUser(management)

            thread.startThread(content,generateId(), user.social.getUserName(), management)
            writeThread(thread.getId(), thread)
            user.social.updateMyThreads(thread.getId(),true)
            writeUser(user.getId(),user)
        }
    } //CREATE

    fun readMainThread(operations: SubThreadOperations){ //READ
        val log = refreshData()
        val management = log.user
        if(verifyPath("users",management) && verifyPath("threads",operations.idMainThread)
                && management != "" && verifyUserSocialProfile(management)){

            val thread = readThread(operations.idMainThread)
            thread.removeSubThread(operations.idSubThread,management)
            readThread(thread.getId())
        }
    }

    //UPDATE
    fun likeThread(like: ThreadOperations){
        val log = refreshData()
        val management = log.user

        if(verifyPath("users",management) && verifyPath("threads",like.idThread)
                && management != "" && verifyUserSocialProfile(management)) {
            val user = readUser(management)
            val thread = readThread(like.idThread)
            val notification = Inbox("${user.social.getUserName()} liked your thread.", "Thread.")


            if(verifyPath("users",thread.getCreator())){
                val userCreator = readUser(thread.getCreator())
                when (checkLikeDislike(thread)) {
                    // 0 -> ALREADY LIKED so do nothing
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
    }


    // 1 -> LIKE; 2 -> DISLIKE
    fun dislikeThread(dislike: ThreadOperations) {
        val log = refreshData()
        val management = log.user

        if(verifyPath("users",management) && verifyPath("threads",dislike.idThread)
                && management != "" && verifyUserSocialProfile(management)) {

            val thread = readThread(dislike.idThread)


            when (checkLikeDislike(thread)) {
                0 -> {
                    thread.likeToDislike(management)
                    writeThread(thread.getId(),thread)
                } // like to dislike
                2 -> {
                    thread.dislike(management)
                    writeThread(thread.getId(),thread)
                } // hasn't DISLIKED yet
            }
        }
    }

    //UPDATE

    fun deleteThread(operations: ThreadOperations){//DELETE
        val log = refreshData()
        val management = log.user

        if(verifyPath("users",management) && verifyPath("threads",operations.idThread)
                && management != "" && verifyUserSocialProfile(management)){

            val user = readUser(management)
            delete(File("${operations.idThread}.json"))
            user.social.updateMyThreads(operations.idThread,false) //FALSE IS TO REMOVE THREAD
            writeUser(user.getId(), user)
        }
    }
    //MAINTHREAD CRUD OPERATIONS


    //SUBTHREAD CRUD OPERATIONS
    fun createSubThread(subThreadData: SubThreadContent){ // CREATE
        val log = refreshData()
        val management = log.user
        if(verifyPath("users",management) && verifyPath("threads",subThreadData.idThread)
                && management != "" && verifyUserSocialProfile(management)){

            val user = readUser(management)
            val thread = readThread(subThreadData.idThread)

            val subThread = SubThread(mutableListOf(),mutableListOf(), management, subThreadData.title, subThreadData.body, user.social.getUserName(), generateId())

            thread.addSubThread(subThread)
            writeThread(thread.getId(), thread)
        }
    }

    //UPDATE
    fun dislikeSubThread(dislike: SubThreadOperations) {
        val log = refreshData()
        val management = log.user


        if(verifyPath("users",management) && verifyPath("threads",dislike.idMainThread)
                && management != "" && verifyUserSocialProfile(management)){

            val thread = readThread(dislike.idMainThread)
            val subThread = thread.getSubThreadById(dislike.idSubThread)

            when (checkLikeDislike(subThread)) {
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
    }

    fun likeSubThread(like: SubThreadOperations){
        val log = refreshData()
        val management = log.user

        if(verifyPath("users",management) && verifyPath("threads",like.idMainThread)
                && management != "" && verifyUserSocialProfile(management)){
            val user = readUser(management)

            val thread = readThread(like.idMainThread)
            val subThread = thread.getSubThreadById(like.idSubThread)
            val notification = Inbox("${user.social.getUserName()} liked your reply.", "Thread.")

            if(verifyPath("users",subThread.creator)){
                val userCreator = readUser(subThread.creator)
                when (checkLikeDislike(subThread)) {
                    1 -> {
                        if(user.getId() != userCreator.getId()){
                            userCreator.social.updateInbox(notification)
                            writeUser(userCreator.getId(), userCreator)
                        }
                        thread.dislikeToLikeSubThread(management,like.idSubThread)
                        writeThread(thread.getId(),thread)
                    } // like to dislike
                    2 -> {
                        if(user.getId() != userCreator.getId()){
                            userCreator.social.updateInbox(notification)
                            writeUser(userCreator.getId(), userCreator)
                        }
                        thread.likeSubThread(management,like.idSubThread)
                        writeThread(thread.getId(),thread)
                    } // hasn't DISLIKED yet
                }
            }
        }
    }
    //UPDATE

    fun deleteSubThread(operations: SubThreadOperations){ //DELETE
        val log = refreshData()
        val management = log.user

        if(verifyPath("users",management) && verifyPath("threads",operations.idMainThread)
                && management != "" && verifyUserSocialProfile(management)){

            val user = readUser(management)
            val thread = readThread(operations.idMainThread)

            thread.removeSubThread(operations.idSubThread,management)
            writeThread(thread.getId(), thread)
        }
    }
    //SUBTHREAD CRUD OPERATIONS

    // 1 -> LIKE; 2 -> DISLIKE

    private fun checkLikeDislike(thread: MainThread): Int {// IF TRUE THE USER ALREADY LIKED OR DISLIKED THE THREAD
        val log = refreshData()
        return when (log.user) {
            in thread.getLikes() // 0 ALREADY LIKED
            -> 0
            in thread.getDislikes() // 1 ALREADY DISLIKED
            -> 1
            else -> 2 // 2 hasn't DISLIKED or liked yet
        }
    }

    private fun checkLikeDislike(thread: SubThread): Int {// IF TRUE THE USER ALREADY LIKED OR DISLIKED THE THREAD
        val log = refreshData()
        val management = log.user
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