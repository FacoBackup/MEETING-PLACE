package br.meetingplace.management.servicies

import br.meetingplace.data.threads.SubThreadContent
import br.meetingplace.data.threads.operations.SubThreadOperations
import br.meetingplace.data.threads.ThreadContent
import br.meetingplace.data.threads.operations.ThreadOperations
import br.meetingplace.entities.user.User
import br.meetingplace.interfaces.Generator
import br.meetingplace.management.GeneralManagement
import br.meetingplace.management.operations.ReadWrite.DeleteFile
import br.meetingplace.management.operations.ReadWrite.ReadWrite
import br.meetingplace.management.operations.verifiers.UserVerifiers
import br.meetingplace.servicies.conversationThread.MainThread
import br.meetingplace.servicies.conversationThread.SubThread
import br.meetingplace.servicies.notification.Inbox
import com.sun.tools.javac.Main
import java.io.File


class ThreadManagement private constructor(): Generator, DeleteFile{

    private val system = GeneralManagement.getManagement()
    private val management = system.getLoggedUser()
    private val verifier = UserVerifiers.getUserVerifier()
    private val generator = generateId()
    private val rw = ReadWrite.getRW()

    companion object{
        private val threadManagement = ThreadManagement()
        fun getManagement() = threadManagement
    }

    fun deleteAllThreadsFromUserId(){
        val fileUser = File("$management.json").exists()

        if(fileUser && management != ""){

            val user = rw.readUser(management)
            val threads = user.social.getMyThreads()

            for(i in 0 until threads.size){
                val fileThread = File("${threads[i]}.json").exists()
                if(fileThread)
                    delete(File("${threads[i]}.json"))
            }
        }
    }

    //MAINTHREAD CRUD OPERATIONS
    fun createMainThread(content: ThreadContent){
        val fileUser = File("$management.json").exists()

        if(fileUser && management != "" && verifier.verifyUserSocialProfile(management)){

            val thread = MainThread()
            val user = rw.readUser(management)

            thread.startThread(content,generator, user.social.getUserName(), management)
            rw.writeThread(thread.getId(), thread)
            user.social.updateMyThreads(thread.getId(),true)
            rw.writeUser(user.getId(),user)
        }
    } //CREATE

    fun readMainThread(operations: SubThreadOperations){ //READ

        val fileUser = File("$management.json").exists()
        val fileThread = File("${operations.idMainThread}.json").exists()
        if(fileUser && fileThread && management != ""){

            val thread = rw.readThread(operations.idMainThread)
            thread.removeSubThread(operations.idSubThread,management)
            rw.readThread(thread.getId())
        }
    }

    //UPDATE
    fun likeThread(like: ThreadOperations){

        val fileUser = File("$management.json").exists()
        val fileThread = File("${like.idThread}.json").exists()
        if(fileUser && fileThread && management != "") {
            val user = rw.readUser(management)
            val thread = rw.readThread(like.idThread)
            val notification = Inbox("${user.social.getUserName()} liked your thread.", "Thread.")
            val fileCreator = File("${thread.getCreator()}.json").exists()

            if(fileCreator){
                val userCreator = rw.readUser(thread.getCreator())
                when (checkLikeDislike(thread)) {
                    // 0 -> ALREADY LIKED so do nothing
                    1-> {// DISLIKED to LIKED
                        if(thread.getCreator() != management){
                            userCreator.social.updateInbox(notification)
                            rw.writeUser(thread.getCreator(),userCreator)
                        }
                            userCreator.social.updateInbox(notification)
                        thread.dislikeToLike(management)
                        rw.writeThread(thread.getId(),thread)
                    }
                    2 -> {// 2 hasn't liked yet
                        if(thread.getCreator() != management){
                            userCreator.social.updateInbox(notification)
                            rw.writeUser(thread.getCreator(),userCreator)
                        }
                        thread.like(management)
                        rw.writeThread(thread.getId(),thread)
                    }
                }
            }
        }
    }


    // 1 -> LIKE; 2 -> DISLIKE
    fun dislikeThread(dislike: ThreadOperations) {

        val fileUser = File("$management.json").exists()
        val fileThread = File("${dislike.idThread}.json").exists()
        if(fileUser && fileThread && management != "") {

            val thread = rw.readThread(dislike.idThread)


            when (checkLikeDislike(thread)) {
                0 -> {
                    thread.likeToDislike(management)
                    rw.writeThread(thread.getId(),thread)
                } // like to dislike
                2 -> {
                    thread.dislike(management)
                    rw.writeThread(thread.getId(),thread)
                } // hasn't DISLIKED yet
            }
        }
    }

    //UPDATE

    fun deleteThread(operations: ThreadOperations){//DELETE

        val fileUser = File("$management.json").exists()
        val fileThread = File("${operations.idThread}.json").exists()
        if(fileUser && fileThread && management != ""){

            val user = rw.readUser(management)

            delete(File("${operations.idThread}.json"))
            user.social.updateMyThreads(operations.idThread,false) //FALSE IS TO REMOVE THREAD
            rw.writeUser(user.getId(), user)
        }
    }
    //MAINTHREAD CRUD OPERATIONS


    //SUBTHREAD CRUD OPERATIONS
    fun createSubThread(subThreadData: SubThreadContent){ // CREATE

        val fileUser = File("$management.json").exists()
        val fileThread = File("${subThreadData.idThread}.json").exists()
        if(fileUser && fileThread && management != ""){

            val user = rw.readUser(management)
            val thread = rw.readThread(subThreadData.idThread)

            val subThread = SubThread(mutableListOf(),mutableListOf(), management, subThreadData.title, subThreadData.body, user.social.getUserName(), generator)

            thread.addSubThread(subThread)
            rw.writeThread(thread.getId(), thread)
        }
    }

    //UPDATE
    fun dislikeSubThread(dislike: SubThreadOperations) {

        val fileUser = File("$management.json").exists()
        val fileThread = File("${dislike.idMainThread}.json").exists()

        if(fileUser && fileThread && management != ""){

            val thread = rw.readThread(dislike.idMainThread)
            val subThread = thread.getSubThreadById(dislike.idSubThread)

            when (checkLikeDislike(subThread)) {
                0 -> {
                    thread.likeToDislikeSubThread(management,dislike.idSubThread)
                    rw.writeThread(thread.getId(),thread)
                } // like to dislike
                2 -> {
                    thread.dislikeSubThread(management,dislike.idSubThread)
                    rw.writeThread(thread.getId(),thread)
                } // hasn't DISLIKED yet
            }
        }
    }

    fun likeSubThread(like: SubThreadOperations){

        val fileUser = File("$management.json").exists()
        val fileThread = File("${like.idMainThread}.json").exists()

        if(fileUser && fileThread && management != ""){
            val user = rw.readUser(management)

            val thread = rw.readThread(like.idMainThread)
            val subThread = thread.getSubThreadById(like.idSubThread)
            val notification = Inbox("${user.social.getUserName()} liked your reply.", "Thread.")
            val fileCreator=  File("${subThread.creator}.json").exists()

            if(fileCreator){
                val userCreator = rw.readUser(subThread.creator)
                when (checkLikeDislike(subThread)) {
                    1 -> {
                        if(user.getId() != userCreator.getId()){
                            userCreator.social.updateInbox(notification)
                            rw.writeUser(userCreator.getId(), userCreator)
                        }
                        thread.dislikeToLikeSubThread(management,like.idSubThread)
                        rw.writeThread(thread.getId(),thread)
                    } // like to dislike
                    2 -> {
                        if(user.getId() != userCreator.getId()){
                            userCreator.social.updateInbox(notification)
                            rw.writeUser(userCreator.getId(), userCreator)
                        }
                        thread.likeSubThread(management,like.idSubThread)
                        rw.writeThread(thread.getId(),thread)
                    } // hasn't DISLIKED yet
                }
            }
        }
    }
    //UPDATE

    fun deleteSubThread(operations: SubThreadOperations){ //DELETE

        val fileUser = File("$management.json").exists()
        val fileThread = File("${operations.idMainThread}.json").exists()
        if(fileUser && fileThread && management != ""){

            val user = rw.readUser(management)
            val thread = rw.readThread(operations.idMainThread)

            thread.removeSubThread(operations.idSubThread,management)
            rw.writeThread(thread.getId(), thread)
        }
    }
    //SUBTHREAD CRUD OPERATIONS

    // 1 -> LIKE; 2 -> DISLIKE

    private fun checkLikeDislike(thread: MainThread): Int {// IF TRUE THE USER ALREADY LIKED OR DISLIKED THE THREAD

        return when (management) {
            in thread.getLikes() // 0 ALREADY LIKED
            -> 0
            in thread.getDislikes() // 1 ALREADY DISLIKED
            -> 1
            else -> 2 // 2 hasn't DISLIKED or liked yet
        }
    }

    private fun checkLikeDislike(thread: SubThread): Int {// IF TRUE THE USER ALREADY LIKED OR DISLIKED THE THREAD
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