package br.meetingplace.management.core.thread

import br.meetingplace.data.threads.ThreadData
import br.meetingplace.data.threads.ThreadOperationsData
import br.meetingplace.management.core.operators.Verify
import br.meetingplace.management.core.operators.fileOperators.rw.ReadWriteCommunity
import br.meetingplace.management.core.operators.fileOperators.rw.ReadWriteThread
import br.meetingplace.management.core.operators.fileOperators.rw.ReadWriteUser
import br.meetingplace.management.core.thread.dependencies.Main
import br.meetingplace.management.core.thread.dependencies.Sub
import br.meetingplace.services.thread.MainThread

class ThreadOperations: Verify, ReadWriteUser, ReadWriteThread, ReadWriteCommunity{

    private fun verifyType(op: ThreadOperationsData?, data: ThreadData?): Int{
        return if(op != null && data == null){
            if(op.idSubThread.isNullOrBlank())
                0 //MAIN
            else
                1 //SUB
        }
        else if(data != null && op == null){
            if(data.idThread != null)
                1 //SUB
            else
                0 //MAIN
        }
        else -1
    }

    fun create(data: ThreadData){ //NEEDS WORK HERE
        when(verifyType(null,data)){
            0->{ //MAIN
                if(data.idCommunity.isNullOrBlank())
                    Main.getMainThreadOperator().create(data)
                else{
                    val community = readCommunity(data.idCommunity)
                    if(verifyCommunity(community)){ //NEEDS THE THREAD ID TO WORK
                        val idThread = Main.getMainThreadOperator().create(data)
                        //the verification for data.idCommunity != null already occurred, so don't mind the !!
                        community.threads.updateThreadsInValidation(idThread!!, null)
                    }
                }

            }
            1->{ //SUB
                if(data.idCommunity.isNullOrBlank())
                    Sub.getSubThreadOperator().create(data)
                else{
                    val community = readCommunity(data.idCommunity)
                    if(verifyCommunity(community) && community.threads.checkThreadApproval(data.idCommunity))
                        Sub.getSubThreadOperator().create(data)
                }
            }
        }
    }

    fun like(data: ThreadOperationsData){

        when(verifyType(data,null)){
            0->{ //MAIN
                if(data.idCommunity.isNullOrBlank())
                    Main.getMainThreadOperator().like(data)
                else{
                    val community = readCommunity(data.idCommunity)
                    if(verifyCommunity(community) && community.threads.checkThreadApproval(data.idCommunity))
                        Main.getMainThreadOperator().like(data)
                }
            }
            1->{ //SUB
                if(data.idCommunity.isNullOrBlank())
                    Sub.getSubThreadOperator().like(data)
                else{
                    val community = readCommunity(data.idCommunity)
                    if(verifyCommunity(community) && community.threads.checkThreadApproval(data.idCommunity))
                        Sub.getSubThreadOperator().like(data)
                }
            }
        }
    }

    fun dislike(data: ThreadOperationsData){
        when(verifyType(data,null)){
            0->{ //MAIN
                if(data.idCommunity.isNullOrBlank())
                    Main.getMainThreadOperator().dislike(data)
                else{
                    val community = readCommunity(data.idCommunity)
                    if(verifyCommunity(community) && community.threads.checkThreadApproval(data.idCommunity))
                        Main.getMainThreadOperator().dislike(data)
                }
            }
            1->{ //SUB
                if(data.idCommunity.isNullOrBlank())
                    Sub.getSubThreadOperator().dislike(data)
                else{
                    val community = readCommunity(data.idCommunity)
                    if(verifyCommunity(community) && community.threads.checkThreadApproval(data.idCommunity))
                        Sub.getSubThreadOperator().dislike(data)
                }
            }
        }
    }

    fun delete(data: ThreadOperationsData){
        when(verifyType(data,null)){
            0->{ //MAIN
                if(data.idCommunity.isNullOrBlank())
                    Main.getMainThreadOperator().delete(data)
                else{
                    val community = readCommunity(data.idCommunity)
                    if(verifyCommunity(community) && community.threads.checkThreadApproval(data.idCommunity)){
                        community.threads.removeApprovedThread(data.idThread)
                        Main.getMainThreadOperator().delete(data)
                    }
                    else if(verifyCommunity(community) && !community.threads.checkThreadApproval(data.idCommunity)){
                        community.threads.updateThreadsInValidation(data.idThread, false)
                        Main.getMainThreadOperator().delete(data)
                    }
                }
            }
            1->{ //SUB
                if(data.idCommunity.isNullOrBlank())
                    Sub.getSubThreadOperator().delete(data)
                else{
                    val community = readCommunity(data.idCommunity)
                    if(verifyCommunity(community) && community.threads.checkThreadApproval(data.idCommunity))
                        Sub.getSubThreadOperator().delete(data)
                }
            }
        }
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

    fun getMyTimeline(): MutableList<MainThread> { //NEEDS WORK HERE
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

            val communities = user.social.getCommunitiesIFollow()
            for(i in 0 until communities.size){
                val community = readCommunity(communities[i])
                if(verifyCommunity(community)){
                    val threads = community.threads.getIdThreads()
                    for(j in 0 until threads.size){
                        val thread = readThread(threads[i])
                        if(verifyThread(thread))
                            myTimeline.add(thread)
                    }
                }
            }

            return myTimeline
        }
        return myTimeline
    }//READ
}