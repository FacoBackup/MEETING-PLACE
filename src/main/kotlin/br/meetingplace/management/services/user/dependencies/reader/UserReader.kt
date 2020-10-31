package br.meetingplace.management.services.user.dependencies.reader

import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.readwrite.controller.ReadWriteController
import br.meetingplace.management.dependencies.verify.controller.VerifyController
import br.meetingplace.services.entitie.User
import br.meetingplace.services.thread.MainThread

class UserReader private constructor():UserReaderInterface {
    private val rw = ReadWriteController.getClass()
    private val verify = VerifyController.getClass()
    private val iDs = IDsController.getClass()

    companion object{
        private val Class = UserReader()
        fun getClass() = Class
    }

    override fun getMyUser(): User {
        val logged = rw.readLoggedUser().email
        return  rw.readUser(logged)
    }//READ

    override fun getMyThreads(): MutableList<MainThread> {
        val loggedUser =  rw.readLoggedUser().email
        val user =  rw.readUser(loggedUser)
        val myThreads = mutableListOf<MainThread>()
        lateinit var myThreadIds: List<String>

        if(verify.verifyUser(user)){
            myThreadIds = user.getMyThreads()

            for (element in myThreadIds){
                val thread =  rw.readThread(element)
                if (verify.verifyThread(thread))
                    myThreads.add(thread)
            }
            return myThreads
        }
        return myThreads
    }//READ

    override fun getMyTimeline(): MutableList<MainThread> { //NEEDS WORK HERE
        val loggedUser =  rw.readLoggedUser().email
        val user =  rw.readUser(loggedUser)
        val myTimeline = mutableListOf<MainThread>()

        if(verify.verifyUser(user)){
            val followingIds = user.getFollowing()

            for (a in followingIds.indices){
                val following =  rw.readUser(followingIds[a].email)
                if(verify.verifyUser(following)){
                    val followingThreads = following.getMyThreads()
                    for (b in followingThreads){
                        val thread =  rw.readThread(b)
                        if (verify.verifyThread(thread))
                            myTimeline.add(thread)
                    }
                }
            }

            val communities = user.getCommunitiesIFollow()
            for(i in communities.indices){
                val community =  rw.readCommunity(communities[i])
                if(verify.verifyCommunity(community)){
                    val threads = community.getIdThreads()
                    for(j in threads.indices){
                        val thread =  rw.readThread(threads[i])
                        if(verify.verifyThread(thread))
                            myTimeline.add(thread)
                    }
                }
            }

            return myTimeline
        }
        return myTimeline
    }//READ
}