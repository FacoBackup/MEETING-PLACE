package br.meetingplace.management.services.community.dependencies.reader

import br.meetingplace.data.Data
import br.meetingplace.management.dependencies.IDs
import br.meetingplace.management.dependencies.Verify
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteCommunity
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteThread
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteUser
import br.meetingplace.services.community.data.Report
import br.meetingplace.services.thread.MainThread

class CommunityReader: ReadWriteLoggedUser, ReadWriteUser,ReadWriteThread, ReadWriteCommunity, IDs, Verify, CommunityReaderInterface{

    override fun seeReports(data: Data): List<Report> {
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val community = readCommunity(getCommunityId(data.ID))

        if(verifyLoggedUser(user) && verifyCommunity(community) && user.getEmail() in community.getModerators())
            return community.getReportedThreads()

        return listOf()
    }
    override fun seeFollowers(data: Data): List<String>{
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val community = readCommunity(getCommunityId(data.ID))

        if(verifyLoggedUser(user) && verifyCommunity(community) && user.getEmail() in community.getModerators())
            return community.getFollowers()

        return listOf()
    }
    override fun seeModerator(data: Data): List<String>{
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val community = readCommunity(getCommunityId(data.ID))

        if(verifyLoggedUser(user) && verifyCommunity(community) && user.getEmail() in community.getModerators())
            return community.getModerators()

        return listOf()
    }
    override fun seeThreads(data: Data): List<MainThread>{
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val community = readCommunity(getCommunityId(data.ID))
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
        val community = readCommunity(getCommunityId(data.ID))

        if(verifyLoggedUser(user) && verifyCommunity(community) && user.getEmail() in community.getModerators())
            return community.getApprovedGroups()

        return listOf()
    }
}