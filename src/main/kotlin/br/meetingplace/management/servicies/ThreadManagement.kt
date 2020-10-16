package br.meetingplace.management.servicies

import br.meetingplace.data.threads.SubThreadContent
import br.meetingplace.data.threads.operations.SubThreadOperations
import br.meetingplace.data.threads.ThreadContent
import br.meetingplace.data.threads.operations.ThreadOperations
import br.meetingplace.entities.user.User
import br.meetingplace.management.GeneralManagement
import br.meetingplace.management.operations.ReadWrite.ReadWrite
import br.meetingplace.servicies.conversationThread.MainThread
import br.meetingplace.servicies.conversationThread.SubThread
import br.meetingplace.servicies.notification.Inbox
import com.sun.tools.javac.Main


open class ThreadManagement{

    private val management = GeneralManagement.getLoggedUser()

    protected fun deleteAllThreadsFromUserId(){
        //READING
        val threadList = mutableListOf<MainThread>()
        val rw = ReadWrite.getRW()
        rw.readThread()?.let { threadList.add(it) }
        //READING
        for(i in 0 until threadList.size){
            if(threadList[i].getCreator() == management){
                val operations = ThreadOperations(threadList[i].getId())
                deleteThread(operations)
            }
        }
    }

    fun createMainThread(content: ThreadContent){
        //READING
        val threadList = mutableListOf<MainThread>()
        val rw = ReadWrite.getRW()
        rw.readThread()?.let { threadList.add(it) }
        //READING

        if(management != "" && verifyUserSocialProfile(management)){
            val thread = MainThread()
            val indexUser = getUserIndex(management)

            thread.startThread(content,generateMainThreadId(), userList[indexUser].social.getUserName(), management)
            threadList.add(thread)
            userList[indexUser].social.updateMyThreadsQuantity(true)
        }
    }

    fun deleteThread(operations: ThreadOperations){

        //READING
        val threadList = mutableListOf<MainThread>()
        val rw = ReadWrite.getRW()
        rw.readThread()?.let { threadList.add(it) }
        //READING

        val indexThread = getThreadIndex(operations.idThread)
        val indexUser = getUserIndex(management)
        if(management != "" && verifyUserSocialProfile(management) && indexThread != -1 && threadList[indexThread].getCreator() == management){
            threadList.remove(threadList[indexThread])
            userList[indexUser].social.updateMyThreadsQuantity(false)
        }
    }

    fun createSubThread(subThreadData: SubThreadContent){

        //READING
        val threadList = mutableListOf<MainThread>()
        val rw = ReadWrite.getRW()
        rw.readThread()?.let { threadList.add(it) }
        //READING

        val indexThread = getThreadIndex(subThreadData.idThread)
        if(management != "" && verifyUserSocialProfile(management) && indexThread != -1){

            val indexUser = getUserIndex(management)
            val idSubThread = generateSubThreadId(threadList[indexThread])
            val subThread = SubThread(mutableListOf(),mutableListOf(), management, subThreadData.title, subThreadData.body, userList[indexUser].social.getUserName(), idSubThread)

            threadList[indexThread].addSubThread(subThread)
        }
    }

    fun deleteSubThread(operations: SubThreadOperations){

        //READING
        val threadList = mutableListOf<MainThread>()
        val rw = ReadWrite.getRW()
        rw.readThread()?.let { threadList.add(it) }
        //READING

        val indexMainThread = getThreadIndex(operations.idMainThread)
        if(management != "" && verifyUserSocialProfile(management) && indexMainThread != -1 && threadList[indexMainThread].getSubThreadCreator(operations.idSubThread) == management)
            threadList[indexMainThread].removeSubThread(operations.idSubThread, management)
    }

    // 1 -> LIKE; 2 -> DISLIKE
    fun likeThread(like: ThreadOperations){

        //READING
        val threadList = mutableListOf<MainThread>()
        val rw = ReadWrite.getRW()
        rw.readThread()?.let { threadList.add(it) }
        //READING

        val indexThread = getThreadIndex(like.idThread)
        val indexCreator = getUserIndex(threadList[indexThread].getCreator())
        val userName = getSocialNameById(management)
        if(management != "" && indexThread != -1 && indexCreator != -1 && verifyUserSocialProfile(management)){

            val notification = Inbox("$userName liked your thread.", "Thread.")
            when (checkLikeDislike(threadList[indexThread])) {
                // 0 -> ALREADY LIKED so do nothing
                1-> {// DISLIKED to LIKED
                    if(threadList[indexThread].getCreator() != management)
                        userList[indexCreator].social.updateInbox(notification)
                    threadList[indexThread].dislikeToLike(management)
                }
                2 -> {// 2 hasn't liked yet
                    if(threadList[indexThread].getCreator() != management)
                        userList[indexCreator].social.updateInbox(notification)
                    threadList[indexThread].like(management)
                }
            }
        }
    }

    fun likeSubThread(like: SubThreadOperations){

        //READING
        val threadList = mutableListOf<MainThread>()
        val rw = ReadWrite.getRW()
        rw.readThread()?.let { threadList.add(it) }
        //READING

        val indexMainThread = getThreadIndex(like.idMainThread)
        val subThread = threadList[indexMainThread].getSubThreadById(like.idSubThread)
        if(management != "" && indexMainThread != -1 && verifyUserSocialProfile(management) && subThread.creator != -1){

            val indexCreator = getUserIndex(subThread.creator)
            val userName = getSocialNameById(management)
            val notification = Inbox("$userName liked your reply.", "Thread.")

            if(indexCreator != -1){
                when (checkLikeDislike(subThread)) {
                    // 0 -> ALREADY LIKED so do nothing
                    1-> {// DISLIKED to LIKED
                        if(threadList[indexMainThread].getSubThreadCreator(like.idSubThread) != management)
                            userList[indexCreator].social.updateInbox(notification)
                        threadList[indexMainThread].dislikeToLikeSubThread(management,like.idSubThread)
                    }
                    2 -> {// 2 hasn't liked yet
                        if(threadList[indexMainThread].getSubThreadCreator(like.idSubThread) != management)
                            userList[indexCreator].social.updateInbox(notification)
                        threadList[indexMainThread].likeSubThread(management,like.idSubThread)
                    }
                }
            }

        }
    }
    // 1 -> LIKE; 2 -> DISLIKE
    fun dislikeThread(dislike: ThreadOperations) {

        val indexThread = getThreadIndex(dislike.idThread)
        //READING
        val threadList = mutableListOf<MainThread>()
        val rw = ReadWrite.getRW()
        rw.readThread()?.let { threadList.add(it) }
        //READING
        if(management != "" && indexThread != -1 && verifyUserSocialProfile(management)){

            when (checkLikeDislike(threadList[indexThread])) {
                0 -> threadList[indexThread].likeToDislike(management) // like to dislike
                2 -> threadList[indexThread].dislike(management) // hasn't DISLIKED yet
            }
        }
    }
    fun dislikeSubThread(dislike: SubThreadOperations) {

        val indexMainThread = getThreadIndex(dislike.idMainThread)
        val subThread = threadList[indexMainThread].getSubThreadById(dislike.idSubThread)
        //READING
        val threadList = mutableListOf<MainThread>()
        val rw = ReadWrite.getRW()
        rw.readThread()?.let { threadList.add(it) }
        //READING
        if(management != "" && indexMainThread != -1 && verifyUserSocialProfile(management) && subThread.creator != -1){

            when (checkLikeDislike(subThread)) {
                0 -> threadList[indexMainThread].likeToDislikeSubThread(management,dislike.idSubThread) // like to dislike
                2 -> threadList[indexMainThread].dislikeSubThread(management,dislike.idSubThread) // hasn't DISLIKED yet
            }
        }
    }

    fun getMyThreads(): MutableList<MainThread> {

        val myThreads = mutableListOf<MainThread>()

        //READING
        val threadList = mutableListOf<MainThread>()
        val rw = ReadWrite.getRW()
        rw.readThread()?.let { threadList.add(it) }
        //READING

        for (i in 0 until threadList.size){
            if (threadList[i].getCreator() == management)
                myThreads.add(threadList[i])
        }
        return myThreads
    }

    fun getMyTimeline(): MutableList<MainThread> {
        val readThread = ReadWrite.getRW()
        //READING
        val threadList = mutableListOf<MainThread>()
        val rw = ReadWrite.getRW()
        rw.readThread()?.let { threadList.add(it) }
        //READING

        val indexUser = getUserIndex(management)
        val followingIds = userList[indexUser].social.following
        val myTimeline = mutableListOf<MainThread>()
        for (i in 0 until threadList.size){
            if (threadList[i].getCreator() in followingIds)
                myTimeline.add(threadList[i])
        }
        return myTimeline
    }

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