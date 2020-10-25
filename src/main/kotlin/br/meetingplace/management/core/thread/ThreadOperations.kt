package br.meetingplace.management.core.thread

import br.meetingplace.data.threads.ThreadData
import br.meetingplace.data.threads.ThreadOperationsData
import br.meetingplace.management.core.operators.Verify
import br.meetingplace.management.core.operators.fileOperators.rw.ReadWriteThread
import br.meetingplace.management.core.operators.fileOperators.rw.ReadWriteUser
import br.meetingplace.management.core.thread.dependencies.Main
import br.meetingplace.management.core.thread.dependencies.Sub
import br.meetingplace.services.thread.MainThread

class ThreadOperations: Verify, ReadWriteUser, ReadWriteThread{
    fun create(data: ThreadData){
        if(data.idThread != null)
            Sub.getSubThreadOperator().create(data)
        else
            Main.getMainThreadOperator().create(data)
    }
    fun like(data: ThreadOperationsData){
        if(data.idSubThread == null)
            Main.getMainThreadOperator().like(data)
        else
            Sub.getSubThreadOperator().like(data)
    }
    fun dislike(data: ThreadOperationsData){
        if(data.idSubThread == null)
            Main.getMainThreadOperator().dislike(data)
        else
            Sub.getSubThreadOperator().dislike(data)
    }
    fun delete(data: ThreadOperationsData){

        if(data.idSubThread == null)
            Main.getMainThreadOperator().delete(data)
        else
            Sub.getSubThreadOperator().delete(data)
    }


    fun getMyThreads(): MutableList<MainThread> {
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val myThreads = mutableListOf<MainThread>()

        if(verifyLoggedUser(user)){
            val myThreadsIds = user.social.getMyThreads()

            for (i in 0 until myThreadsIds.size){
                val thread = readThread(myThreadsIds[i])
                if (verifyThread(thread))
                    myThreads.add(thread)
            }
            return myThreads
        }
        return myThreads
    }//READ

    fun getMyTimeline(): MutableList<MainThread> {
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val myTimeline = mutableListOf<MainThread>()

        if(verifyLoggedUser(user)){
            val followingIds = user.social.getFollowing()

            for (i in 0 until followingIds.size){
                val following = readUser(followingIds[i])
                if( verifyUser(following)){
                    val followingThreads = following.social.getMyThreads()
                    for (j in 0 until followingThreads.size){
                        val thread = readThread(followingThreads[j])
                        if (verifyThread(thread))
                            myTimeline.add(thread)
                    }
                }
            }
            return myTimeline
        }
        return myTimeline
    }//READ
}