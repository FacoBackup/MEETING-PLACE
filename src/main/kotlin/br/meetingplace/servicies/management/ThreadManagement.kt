package br.meetingplace.servicies.management

import br.meetingplace.data.Operations
import br.meetingplace.data.ThreadContent
import br.meetingplace.data.ThreadOperations
import br.meetingplace.servicies.conversationThread.MainThread


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
    /*
    fun createSubThread(content: ThreadContent, subThread: ThreadOperations){

        if(getLoggedUser() != -1){

            val indexMainThreadCreator = getUserIndex(subThread.idUser)
            val indexSubThreadCreator = getUserIndex(getLoggedUser())
            val indexThread = if(getThreadIndex(subThread.idThread) == -1) getThreadIndex(subThread.idThread, subThread.idUser) else getThreadIndex(subThread.idThread)
            val thread = SubThread()

            val idSubThread = generateSubThreadId(threadsCopy[indexThread])

            thread.startThread(content, idSubThread, userList[indexSubThreadCreator].social.userName, getLoggedUser())
            userList[indexMainThreadCreator].social.threadOperations(indexThread,thread)
        }
    }

     */
    // 1 -> LIKE; 2 -> DISLIKE
    fun likeThread(like: ThreadOperations){

        val indexUser = getUserIndex(like.idUser)

        if(getLoggedUser() != -1 && indexUser != -1  && verifyUserSocialProfile(getLoggedUser())){

            val indexThread = if(getThreadIndex(like.idThread) == -1) getThreadIndex(like.idThread, like.idUser) else getThreadIndex(like.idThread)
            val thread = userList[indexUser].social.getThreadById(like.idThread)

            if (thread.getId() != -1){

                if(!checkLikeDislike(thread)) //MAIN THREAD
                    userList[indexUser].social.threadOperations(indexThread,1,like.idUser)
            }
        }
    }
    // 1 -> LIKE; 2 -> DISLIKE
    fun dislikeThread(dislike: ThreadOperations){

        val indexUser = getUserIndex(dislike.idUser)


        if(getLoggedUser() != -1 && indexUser != -1  && verifyUserSocialProfile(getLoggedUser())){

            val indexThread = if(getThreadIndex(dislike.idThread) == -1) getThreadIndex(dislike.idThread, dislike.idUser) else getThreadIndex(dislike.idThread)
            val thread = userList[indexUser].social.getThreadById(dislike.idThread)

            if (thread.getId() != -1){

                if(!checkLikeDislike(thread)) //MAIN THREAD
                    userList[indexUser].social.threadOperations(indexThread,2,dislike.idUser)
            }
        }
    }

    private fun checkLikeDislike(thread: MainThread): Boolean {// IF TRUE THE USER ALREADY LIKED OR DISLIKED THE THREAD

        return getLoggedUser() in thread.getLikes() || getLoggedUser() in thread.getDislikes()
    }
}