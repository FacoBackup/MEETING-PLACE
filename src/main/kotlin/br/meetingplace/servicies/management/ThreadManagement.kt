package br.meetingplace.servicies.management

import br.meetingplace.data.Operations
import br.meetingplace.data.threads.SubThreadData
import br.meetingplace.data.threads.ThreadContent
import br.meetingplace.data.threads.ThreadOperations
import br.meetingplace.servicies.conversationThread.MainThread
import br.meetingplace.servicies.conversationThread.SubThread
import br.meetingplace.servicies.notification.Inbox


open class ThreadManagement:GeneralManagement() {

    fun createMainThread(content: ThreadContent){

        if(getLoggedUser() != -1 && verifyUserSocialProfile(getLoggedUser())){
            val thread = MainThread()
            val indexUser = getUserIndex(getLoggedUser())

            thread.startThread(content,generateMainThreadId(), userList[indexUser].social.userName, getLoggedUser())
            threadList.add(thread)
            val indexThread = getThreadIndex(thread.getId())
            addThread(indexThread)
        }
    }

    fun deleteThread(operations: Operations){

        val indexThread = getThreadIndex(operations.id)
        if(getLoggedUser() != -1 && operations.pass == cachedPass && verifyUserSocialProfile(getLoggedUser()) && indexThread != -1 && threadList[indexThread].getCreator() == getLoggedUser()){
                threadList.remove(threadList[indexThread])
                removeThread(indexThread)
        }
    }

    fun createSubThread(operations: SubThreadData){

        if(getLoggedUser() != -1 && verifyUserSocialProfile(getLoggedUser())){

            //val indexMainThreadCreator = getUserIndex(operations.idCreator)
            val indexSubThreadCreator = getUserIndex(getLoggedUser())
            val indexThread = getThreadIndex(operations.idThread)
            val subThread = SubThread()

            if(indexThread != -1){
                val idSubThread = generateSubThreadId(threadList[indexThread])
                val threadContent = ThreadContent(operations.title, operations.body)

                subThread.startThread(threadContent, idSubThread, userList[indexSubThreadCreator].social.userName, getLoggedUser())
                threadList[indexThread].addSubThread(subThread)
                updateThread(indexThread)
            }
        }
    }

    fun deleteSubThread(operations: SubThreadData){

        val indexThread = getThreadIndex(operations.idThread)

        if(getLoggedUser() != -1 && verifyUserSocialProfile(getLoggedUser()) && indexThread != -1){
            threadList[indexThread].removeSubThread(operations.idSubThread, operations.idCreator)
            updateThread(indexThread)
        }

    }

    // 1 -> LIKE; 2 -> DISLIKE
    fun likeThread(like: ThreadOperations){

        val indexThread = getThreadIndex(like.idThread)
        val indexUser = getUserIndex(like.idUser)
        if(getLoggedUser() != -1 && indexThread != -1 && indexUser != -1 && verifyUserSocialProfile(getLoggedUser())){

            val notification = Inbox("${userList[indexUser].social.userName} liked your thread.", "Thread.")
            when (checkLikeDislike(threadList[indexThread])) {
                // 0 -> ALREADY LIKED so do nothing
                1-> {// DISLIKED to LIKED
                    if(like.idUser != getLoggedUser())
                        userList[indexUser].social.updateInbox(notification)
                    threadList[indexThread].dislikeToLike(getLoggedUser())
                    updateThread(indexThread)
                }
                2 -> {// 2 hasn't liked yet
                    if(like.idUser != getLoggedUser())
                        userList[indexUser].social.updateInbox(notification)
                    threadList[indexThread].like(getLoggedUser())
                    updateThread(indexThread)
                }
            }
        }
    }
    // 1 -> LIKE; 2 -> DISLIKE
    fun dislikeThread(dislike: ThreadOperations) {

        val indexUser = getUserIndex(dislike.idUser)
        val indexThread = getThreadIndex(dislike.idThread)
        if(getLoggedUser() != -1 && indexThread != -1 && indexUser != -1 && verifyUserSocialProfile(getLoggedUser())){

            when (checkLikeDislike(threadList[indexThread])) {
                0 ->{
                    threadList[indexThread].likeToDislike(getLoggedUser()) // like to dislike
                    updateThread(indexThread)
                }
                2 -> {
                    threadList[indexThread].dislike(getLoggedUser())
                    updateThread(indexThread)
                } // hasn't DISLIKED yet
            }
        }
    }


    private fun addThread(indexThread: Int){
        println(indexThread)

        val indexUser = getUserIndex(getLoggedUser())
        val userFollowers = userList[indexUser].social.followers
        println(indexUser)

        if(indexUser != -1 && indexThread != -1){
            if(userFollowers.size > 0)
                for(i in 0 until userFollowers.size)
                    userList[userFollowers[i]].social.addTimelineThread(threadList[indexThread])

            userList[indexUser].social.addMyThread(threadList[indexThread])
        }

    }

    private fun removeThread(indexThread: Int){

        val indexUser = getUserIndex(getLoggedUser())
        val userFollowers = userList[indexUser].social.followers

        if(indexUser != -1 && indexThread != -1) {
            if(userFollowers.size > 0)
                for (i in 0 until userFollowers.size)
                    userList[userFollowers[i]].social.removeTimelineThread(threadList[indexThread])

            userList[indexUser].social.removeMyThread(threadList[indexThread])
        }
    }

    private fun updateThread(indexThread: Int){

        val indexUser = getUserIndex(threadList[indexThread].getCreator())
        val userFollowers = userList[indexUser].social.followers

        if(indexUser != -1 && indexThread != -1) {
            if(userFollowers.size > 0)
                for(i in 0 until userFollowers.size)
                    userList[userFollowers[i]].social.updateTimeline(threadList[indexThread])

            userList[indexUser].social.updateMyThreads(threadList[indexThread])
        }

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
}