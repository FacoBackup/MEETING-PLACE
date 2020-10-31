package br.meetingplace.management.services.community.dependencies.reader

import br.meetingplace.data.Data
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.readwrite.controller.ReadWriteController
import br.meetingplace.management.dependencies.verify.controller.VerifyController
import br.meetingplace.services.community.data.Report
import br.meetingplace.services.thread.MainThread

class CommunityReader: CommunityReaderInterface{
    private val rw = ReadWriteController.getClass()
    private val verify = VerifyController.getClass()
    private val iDs = IDsController.getClass()

    override fun seeReports(data: Data): List<Report> {
        val logged = rw.readLoggedUser().fileName
        val user = rw.readUser(if(!logged.isNullOrBlank()) logged else "")

        val community =  rw.readCommunity(iDs.getCommunityId(data.ID))

        if(verify.verifyUser(user) && verify.verifyCommunity(community) && user.getEmail() in community.getModerators())
            return community.getReportedThreads()

        return listOf()
    }

    override fun seeFollowers(data: Data): List<String>{
        val logged = rw.readLoggedUser().fileName
        val user = rw.readUser(if(!logged.isNullOrBlank()) logged else "")

        val community =  rw.readCommunity(iDs.getCommunityId(data.ID))

        if(verify.verifyUser(user) && verify.verifyCommunity(community) && user.getEmail() in community.getModerators())
            return community.getFollowers()

        return listOf()
    }
    
    override fun seeModerators(data: Data): List<String>{
        val logged = rw.readLoggedUser().fileName
        val user = rw.readUser(if(!logged.isNullOrBlank()) logged else "")

        val community =  rw.readCommunity(iDs.getCommunityId(data.ID))

        if(verify.verifyUser(user) && verify.verifyCommunity(community) && user.getEmail() in community.getModerators())
            return community.getModerators()

        return listOf()
    }
    
    override fun seeThreads(data: Data): List<MainThread>{
        val logged = rw.readLoggedUser().fileName
        val user = rw.readUser(if(!logged.isNullOrBlank()) logged else "")

        val community = rw.readCommunity(iDs.getCommunityId(data.ID))
        lateinit var threads: List<String>
        
        if(verify.verifyUser(user) && verify.verifyCommunity(community) && user.getEmail() in community.getModerators()){
            threads = community.getIdThreads()

            val communityThreads = mutableListOf<MainThread>()

            for (element in threads){
                val thread =  rw.readThread(element)
                if(verify.verifyThread(thread))
                    communityThreads.add(thread)
            }

            return communityThreads
        }

        return listOf()
    }
    override fun seeGroups(data: Data): List<String>{
        val loggedUser = rw.readLoggedUser().email
        val user =  rw.readUser(loggedUser)
        val community =  rw.readCommunity(iDs.getCommunityId(data.ID))

        if(verify.verifyUser(user) && verify.verifyCommunity(community) && user.getEmail() in community.getModerators())
            return community.getApprovedGroups()

        return listOf()
    }
}