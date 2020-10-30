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
        lateinit var communityList: List<String>
        if (verifyLoggedUser(user)){
            communityList = user.getCommunitiesIFollow()
            for(element in communityList){
                val data = readCommunity(element)
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
        lateinit var communityList: List<String>
        if (verifyLoggedUser(user)){
            communityList = user.getModeratorIn()
            for(element in communityList){
                val data = readCommunity(element)
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
        lateinit var myThreadIds: List<String>

        if(verifyLoggedUser(user)){
            myThreadIds = user.getMyThreads()

            for (element in myThreadIds){
                val thread = readThread(element)
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
            val followingIds = user.getFollowing()

            for (element in followingIds){
                val following = readUser(element)
                if( verifyUser(following)){
                    val followingThreads = following.getMyThreads()
                    for (element in followingThreads){
                        val thread = readThread(element)
                        if (verifyThread(thread))
                            myTimeline.add(thread)
                    }
                }
            }

            val communities = user.getCommunitiesIFollow()
            for(i in communities.indices){
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