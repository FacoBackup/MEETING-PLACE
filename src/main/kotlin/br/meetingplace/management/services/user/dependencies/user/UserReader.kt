package br.meetingplace.management.services.user.dependencies.user

import br.meetingplace.management.dependencies.Verify
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteCommunity
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteThread
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteUser
import br.meetingplace.services.community.Community
import br.meetingplace.services.entitie.User
import br.meetingplace.services.thread.MainThread

abstract class UserReader:  Verify, ReadWriteLoggedUser, ReadWriteUser, ReadWriteThread, ReadWriteCommunity {

    fun getMyUser(): User {
        val logged = readLoggedUser().email
        return readUser(logged)
    }//READ

    fun communitiesIFollow(): List<Community>{
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val communities = mutableListOf<Community>()

        if (verifyLoggedUser(user)){
            val communityList = user.social.getCommunitiesIFollow()
            for(i in 0 until communityList.size){
                val data = readCommunity(communityList[i])
                if(verifyCommunity(data))
                    communities.add(data)
            }
        }
        return communities
    }

    fun moderatorIn(): List<Community>{
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val communities = mutableListOf<Community>()

        if (verifyLoggedUser(user)){
            val communityList = user.social.getModeratorIn()
            for(i in 0 until communityList.size){
                val data = readCommunity(communityList[i])
                if(verifyCommunity(data))
                    communities.add(data)
            }
        }
        return communities
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