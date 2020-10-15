package br.meetingplace.management.servicies

import br.meetingplace.data.threads.SubThreadContent
import br.meetingplace.data.threads.operations.SubThreadOperations
import br.meetingplace.data.threads.ThreadContent
import br.meetingplace.data.threads.operations.ThreadOperations
import br.meetingplace.servicies.conversationThread.MainThread
import br.meetingplace.servicies.conversationThread.SubThread
import br.meetingplace.servicies.notification.Inbox


open class ThreadManagement: ChatManagement() {

    protected fun deleteAllThreadsFromUserId(){
        for(i in 0 until threadList.size){
            if(threadList[i].getCreator() == getLoggedUser()){
                val operations = ThreadOperations(threadList[i].getId())
                deleteThread(operations)
            }
        }
    }

    fun createMainThread(content: ThreadContent){

        if(getLoggedUser() != -1 && verifyUserSocialProfile(getLoggedUser())){
            val thread = MainThread()
            val indexUser = getUserIndex(getLoggedUser())

            thread.startThread(content,generateMainThreadId(), userList[indexUser].social.getUserName(), getLoggedUser())
            threadList.add(thread)
            userList[indexUser].social.updateMyThreadsQuantity(true)
        }
    }

    fun deleteThread(operations: ThreadOperations){

        val indexThread = getThreadIndex(operations.idThread)
        val indexUser = getUserIndex(getLoggedUser())
        if(getLoggedUser() != -1 && verifyUserSocialProfile(getLoggedUser()) && indexThread != -1 && threadList[indexThread].getCreator() == getLoggedUser()){
            threadList.remove(threadList[indexThread])
            userList[indexUser].social.updateMyThreadsQuantity(false)
        }
    }

    fun createSubThread(subThreadData: SubThreadContent){

        val indexThread = getThreadIndex(subThreadData.idThread)
        if(getLoggedUser() != -1 && verifyUserSocialProfile(getLoggedUser()) && indexThread != -1){

            val indexUser = getUserIndex(getLoggedUser())
            val idSubThread = generateSubThreadId(threadList[indexThread])
            val subThread = SubThread(mutableListOf(),mutableListOf(), subThreadData.idCreator, subThreadData.title, subThreadData.body, userList[indexUser].social.getUserName(), idSubThread)

            threadList[indexThread].addSubThread(subThread)
        }
    }

    fun deleteSubThread(operations: SubThreadOperations){

        val indexMainThread = getThreadIndex(operations.idMainThread)
        if(getLoggedUser() != -1 && verifyUserSocialProfile(getLoggedUser()) && indexMainThread != -1 && threadList[indexMainThread].getSubThreadCreator(operations.idSubThread) == getLoggedUser())
            threadList[indexMainThread].removeSubThread(operations.idSubThread, getLoggedUser())
    }

    // 1 -> LIKE; 2 -> DISLIKE
    fun likeThread(like: ThreadOperations){

        val indexThread = getThreadIndex(like.idThread)
        val indexCreator = getUserIndex(threadList[indexThread].getCreator())
        val userName = getSocialNameById(getLoggedUser())
        if(getLoggedUser() != -1 && indexThread != -1 && indexCreator != -1 && verifyUserSocialProfile(getLoggedUser())){

            val notification = Inbox("$userName liked your thread.", "Thread.")
            when (checkLikeDislike(threadList[indexThread])) {
                // 0 -> ALREADY LIKED so do nothing
                1-> {// DISLIKED to LIKED
                    if(threadList[indexThread].getCreator() != getLoggedUser())
                        userList[indexCreator].social.updateInbox(notification)
                    threadList[indexThread].dislikeToLike(getLoggedUser())
                }
                2 -> {// 2 hasn't liked yet
                    if(threadList[indexThread].getCreator() != getLoggedUser())
                        userList[indexCreator].social.updateInbox(notification)
                    threadList[indexThread].like(getLoggedUser())
                }
            }
        }
    }

    fun likeSubThread(like: SubThreadOperations){

        val indexMainThread = getThreadIndex(like.idMainThread)
        val subThread = threadList[indexMainThread].getSubThreadById(like.idSubThread)
        if(getLoggedUser() != -1 && indexMainThread != -1 && verifyUserSocialProfile(getLoggedUser()) && subThread.creator != -1){

            val indexCreator = getUserIndex(threadList[indexMainThread].getSubThreadCreator(like.idSubThread))
            val userName = getSocialNameById(getLoggedUser())
            val notification = Inbox("$userName liked your reply.", "Thread.")

            when (checkLikeDislike(subThread)) {
                // 0 -> ALREADY LIKED so do nothing
                1-> {// DISLIKED to LIKED
                    if(threadList[indexMainThread].getSubThreadCreator(like.idSubThread) != getLoggedUser())
                        userList[indexCreator].social.updateInbox(notification)
                    threadList[indexMainThread].dislikeToLikeSubThread(getLoggedUser(),like.idSubThread)
                }
                2 -> {// 2 hasn't liked yet
                    if(threadList[indexMainThread].getSubThreadCreator(like.idSubThread) != getLoggedUser())
                        userList[indexCreator].social.updateInbox(notification)
                    threadList[indexMainThread].likeSubThread(getLoggedUser(),like.idSubThread)
                }
            }
        }
    }
    // 1 -> LIKE; 2 -> DISLIKE
    fun dislikeThread(dislike: ThreadOperations) {

        val indexThread = getThreadIndex(dislike.idThread)
        if(getLoggedUser() != -1 && indexThread != -1 && verifyUserSocialProfile(getLoggedUser())){

            when (checkLikeDislike(threadList[indexThread])) {
                0 -> threadList[indexThread].likeToDislike(getLoggedUser()) // like to dislike
                2 -> threadList[indexThread].dislike(getLoggedUser()) // hasn't DISLIKED yet
            }
        }
    }
    fun dislikeSubThread(dislike: SubThreadOperations) {

        val indexMainThread = getThreadIndex(dislike.idMainThread)
        val subThread = threadList[indexMainThread].getSubThreadById(dislike.idSubThread)

        if(getLoggedUser() != -1 && indexMainThread != -1 && verifyUserSocialProfile(getLoggedUser()) && subThread.creator != -1){

            when (checkLikeDislike(subThread)) {
                0 -> threadList[indexMainThread].likeToDislikeSubThread(getLoggedUser(),dislike.idSubThread) // like to dislike
                2 -> threadList[indexMainThread].dislikeSubThread(getLoggedUser(),dislike.idSubThread) // hasn't DISLIKED yet
            }
        }
    }

    fun getMyThreads(): MutableList<MainThread> {

        val myThreads = mutableListOf<MainThread>()

        for (i in 0 until threadList.size){
            if (threadList[i].getCreator() == getLoggedUser())
                myThreads.add(threadList[i])
        }
        return myThreads
    }

    fun getMyTimeline(): MutableList<MainThread> {

        val indexUser = getUserIndex(getLoggedUser())
        val followingIds = userList[indexUser].social.following
        val myTimeline = mutableListOf<MainThread>()
        for (i in 0 until threadList.size){
            if (threadList[i].getCreator() in followingIds)
                myTimeline.add(threadList[i])
        }
        return myTimeline
    }

    private fun checkLikeDislike(thread: MainThread): Int {// IF TRUE THE USER ALREADY LIKED OR DISLIKED THE THREAD

        return when {
            getLoggedUser() in thread.getLikes() // 0 ALREADY LIKED
            -> 0
            getLoggedUser() in thread.getDislikes() // 1 ALREADY DISLIKED
            -> 1
            else -> 2 // 2 hasn't DISLIKED or liked yet
        }
    }

    private fun checkLikeDislike(thread: SubThread): Int {// IF TRUE THE USER ALREADY LIKED OR DISLIKED THE THREAD
        return if(thread.id != -1){
            when {
                getLoggedUser() in thread.likes // 0 ALREADY LIKED
                -> 0
                getLoggedUser() in thread.dislikes// 1 ALREADY DISLIKED
                -> 1
                else -> 2 // 2 hasn't DISLIKED or liked yet
            }
        }
        else 3 // SubThread Doesn't exist
    }
}