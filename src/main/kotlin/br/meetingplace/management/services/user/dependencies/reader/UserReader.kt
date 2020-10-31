package br.meetingplace.management.services.user.dependencies.reader

import br.meetingplace.management.dependencies.Verify
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteCommunity
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteThread
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteUser
import br.meetingplace.services.community.Community
import br.meetingplace.services.entitie.User
import br.meetingplace.services.thread.MainThread

class UserReader private constructor():  Verify, ReadWriteLoggedUser, ReadWriteUser, ReadWriteThread, ReadWriteCommunity, UserReaderInterface {

    companion object{
        private val Class = UserReader()
        fun getClass() = Class
    }

    override fun getMyUser(): User {
        val logged = readLoggedUser().email
        return readUser(logged)
    }//READ

    override fun getMyThreads(): MutableList<MainThread> {
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

    override fun getMyTimeline(): MutableList<MainThread> { //NEEDS WORK HERE
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val myTimeline = mutableListOf<MainThread>()

        if(verifyLoggedUser(user)){
            val followingIds = user.getFollowing()

            for (a in followingIds.indices){
                val following = readUser(followingIds[a].email)
                if( verifyUser(following)){
                    val followingThreads = following.getMyThreads()
                    for (b in followingThreads){
                        val thread = readThread(b)
                        if (verifyThread(thread))
                            myTimeline.add(thread)
                    }
                }
            }

            val communities = user.getCommunitiesIFollow()
            for(i in communities.indices){
                val community = readCommunity(communities[i])
                if(verifyCommunity(community)){
                    val threads = community.getIdThreads()
                    for(j in threads.indices){
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