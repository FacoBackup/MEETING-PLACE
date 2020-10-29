package br.meetingplace.management.services.community.dependencies.followers

import br.meetingplace.data.community.ReportData
import br.meetingplace.management.dependencies.Generator
import br.meetingplace.management.dependencies.IDs
import br.meetingplace.management.dependencies.Verify
import br.meetingplace.management.dependencies.fileOperators.rw.*
import br.meetingplace.services.community.data.Report

class Follower private constructor():FollowerInterface, ReadWriteCommunity, ReadWriteUser, ReadWriteLoggedUser, IDs, ReadWriteThread, Verify, Generator, ReadWriteReport{

    companion object{
        private val Class = Follower()
        fun getClass () = Class
    }
    override fun createReport(data: ReportData){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val community = readCommunity(getCommunityId(data.communityId))
        val thread = readThread(data.idService)

        if (verifyLoggedUser(user) && verifyCommunity(community) && verifyThread(thread)){
            val newReport = Report(generateId(), loggedUser, data.idService, data.reason, false, community.getId(), null)
            community.threads.updateReport(newReport, false)
            writeCommunity(community, community.getId())
            writeReport(newReport, newReport.reportId)
        }
    }

    override fun deleteReport(data: ReportData) {
        TODO("Not yet implemented")
    }
}