package br.meetingplace.management.services.community.dependencies.reader

import br.meetingplace.data.Data
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.verify.dependencies.Verify
import br.meetingplace.management.dependencies.readwrite.dependencies.community.ReadWriteCommunity
import br.meetingplace.management.dependencies.readwrite.dependencies.user.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.readwrite.dependencies.thread.ReadWriteThread
import br.meetingplace.management.dependencies.readwrite.dependencies.user.ReadWriteUser
import br.meetingplace.services.community.data.Report
import br.meetingplace.services.thread.MainThread

class CommunityReader: ReadWriteLoggedUser, ReadWriteUser, ReadWriteThread, ReadWriteCommunity, Verify, CommunityReaderInterface{
    private val iDs = IDsController.getClass()
    override fun seeReports(data: Data): List<Report> {
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val community = readCommunity(iDs.getCommunityId(data.ID))

        if(verifyLoggedUser(user) && verifyCommunity(community) && user.getEmail() in community.getModerators())
            return community.getReportedThreads()

        return listOf()
    }

    override fun seeFollowers(data: Data): List<String>{
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val community = readCommunity(iDs.getCommunityId(data.ID))

        if(verifyLoggedUser(user) && verifyCommunity(community) && user.getEmail() in community.getModerators())
            return community.getFollowers()

        return listOf()
    }
    
    override fun seeModerators(data: Data): List<String>{
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val community = readCommunity(iDs.getCommunityId(data.ID))

        if(verifyLoggedUser(user) && verifyCommunity(community) && user.getEmail() in community.getModerators())
            return community.getModerators()

        return listOf()
    }
    
    override fun seeThreads(data: Data): List<MainThread>{
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val community = readCommunity(iDs.getCommunityId(data.ID))
        lateinit var threads: List<String>
        
        if(verifyLoggedUser(user) && verifyCommunity(community) && user.getEmail() in community.getModerators()){
            threads = community.getIdThreads()

            val communityThreads = mutableListOf<MainThread>()

            for (element in threads){
                val thread = readThread(element)
                if(verifyThread(thread))
                    communityThreads.add(thread)
            }

            return communityThreads
        }

        return listOf()
    }
    override fun seeGroups(data: Data): List<String>{
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val community = readCommunity(iDs.getCommunityId(data.ID))

        if(verifyLoggedUser(user) && verifyCommunity(community) && user.getEmail() in community.getModerators())
            return community.getApprovedGroups()

        return listOf()
    }
}