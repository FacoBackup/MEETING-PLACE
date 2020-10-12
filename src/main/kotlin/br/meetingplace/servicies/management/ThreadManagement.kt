package br.meetingplace.servicies.management

import br.meetingplace.data.Operations
import br.meetingplace.data.SubThreadData
import br.meetingplace.data.ThreadContent
import br.meetingplace.data.ThreadOperations
import br.meetingplace.servicies.conversationThread.MainThread
import br.meetingplace.servicies.conversationThread.SubThread
import br.meetingplace.servicies.notification.Inbox


open class ThreadManagement:GeneralManagement() {

    fun createMainThread(content: ThreadContent){

        if(getLoggedUser() != -1 && verifyUserSocialProfile(getLoggedUser())){
            val thread = MainThread()
            val indexUser = getUserIndex(getLoggedUser())
            val id = generateMainThreadId(userList[indexUser].social.getThreadId())
            thread.startThread(content, id, userList[indexUser].social.userName, getLoggedUser())
            userList[indexUser].social.addNewThread(thread)
        }

    }

    fun deleteThread(operations: Operations){

        if(getLoggedUser() != -1 && operations.pass == cachedPass && verifyUserSocialProfile(getLoggedUser())){
            val indexThread = getThreadIndex(operations.id)
            val indexUser = getUserIndex(getLoggedUser())

            userList[indexUser].social.removeThread(indexThread)
        }

    }

    fun createSubThread(operations: SubThreadData){

        if(getLoggedUser() != -1 && verifyUserSocialProfile(getLoggedUser())){

            val indexMainThreadCreator = getUserIndex(operations.idCreator)
            val indexSubThreadCreator = getUserIndex(getLoggedUser())
            val indexThread = if(getThreadIndex(operations.idThread) == -1) getThreadIndex(operations.idThread, operations.idCreator) else getThreadIndex(operations.idThread)
            val newThread = SubThread()

            if(indexThread != -1){
                val idSubThread = generateSubThreadId(userList[indexMainThreadCreator].social.getThreadById(operations.idThread))
                val threadContent = ThreadContent( operations.title, operations.body)

                newThread.startThread(threadContent, idSubThread, userList[indexSubThreadCreator].social.userName, getLoggedUser())
                userList[indexMainThreadCreator].social.threadOperations(indexThread,newThread)
            }
        }
    }

    fun deleteSubThread(operations: SubThreadData){

        if(getLoggedUser() != -1 && verifyUserSocialProfile(getLoggedUser())){
            val indexThread = getThreadIndex(operations.idThread)
            val indexMainCreator = getUserIndex(operations.idCreator)
            userList[indexMainCreator].social.subThreadOperations(indexThread,operations.idSubThread, getLoggedUser())
        }
    }

    // 1 -> LIKE; 2 -> DISLIKE
    fun likeThread(like: ThreadOperations){

        val indexUser = getUserIndex(like.idUser)


        if(getLoggedUser() != -1 && indexUser != -1  && verifyUserSocialProfile(getLoggedUser())){

            val indexThread = if(getThreadIndex(like.idThread) == -1) getThreadIndex(like.idThread, like.idUser) else getThreadIndex(like.idThread)
            val thread = userList[indexUser].social.getThreadById(like.idThread)
            val notification = Inbox("${userList[indexUser].social.userName} liked your thread.", "Thread.")

            if (thread.getId() != -1)
                when (checkLikeDislike(thread)) {
                    // 0 -> ALREADY LIKED so do nothing
                    1-> {// DISLIKED to LIKED
                        if(like.idUser != getLoggedUser())
                            userList[indexUser].social.updateInbox(notification)
                        userList[indexUser].social.threadOperations(indexThread, -1, like.idUser)// -1 CHANGE FROM DISLIKED TO LIKED
                    }
                    2 -> {// 2 hasn't DISLIKED or liked yet
                        if(like.idUser != getLoggedUser())
                            userList[indexUser].social.updateInbox(notification)
                        userList[indexUser].social.threadOperations(indexThread, 1, like.idUser)
                    }
                }

        }
    }
    // 1 -> LIKE; 2 -> DISLIKE
    fun dislikeThread(dislike: ThreadOperations){

        val indexUser = getUserIndex(dislike.idUser)

        if(getLoggedUser() != -1 && indexUser != -1  && verifyUserSocialProfile(getLoggedUser())){

            val indexThread = if(getThreadIndex(dislike.idThread) == -1) getThreadIndex(dislike.idThread, dislike.idUser) else getThreadIndex(dislike.idThread)
            val thread = userList[indexUser].social.getThreadById(dislike.idThread)

            val notification = Inbox("${userList[indexUser].social.userName} disliked your thread.", "Thread.")

            if (thread.getId() != -1)
                when (checkLikeDislike(thread)) {
                    // -1 CHANGE FROM DISLIKED TO LIKED
                    0 -> {// 0 ALREADY LIKED
                        if(dislike.idUser != getLoggedUser())
                            userList[indexUser].social.updateInbox(notification)
                        userList[indexUser].social.threadOperations(indexThread, -2, dislike.idUser)
                    }
                    //1 ALREADY DISLIKED so do nothing
                    2 -> {// 2 hasn't DISLIKED or liked yet
                        if(dislike.idUser != getLoggedUser())
                            userList[indexUser].social.updateInbox(notification)
                        userList[indexUser].social.threadOperations(indexThread, 2, dislike.idUser)
                    }
                }
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