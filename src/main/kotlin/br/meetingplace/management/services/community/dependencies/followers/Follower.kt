package br.meetingplace.management.services.community.dependencies.followers

import br.meetingplace.data.community.ApprovalData
import br.meetingplace.data.community.ReportData
import br.meetingplace.management.dependencies.IDs
import br.meetingplace.management.dependencies.Verify
import br.meetingplace.management.dependencies.fileOperators.DeleteFile
import br.meetingplace.management.dependencies.fileOperators.rw.*
import br.meetingplace.services.community.data.Report

class Follower private constructor():FollowerInterface, ReadWriteCommunity, ReadWriteUser, ReadWriteLoggedUser, IDs, ReadWriteThread, Verify, ReadWriteReport{

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
            community.updateReport(newReport, false)
            writeCommunity(community, community.getId())
            writeReport(newReport, newReport.reportId)
        }
    }

    override fun deleteReport(data: ApprovalData) {
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val community = readCommunity(getCommunityId(data.community))
        val report = readReport(data.id)

        if (verifyLoggedUser(user) && verifyCommunity(community) && verifyReport(report) && (loggedUser in community.getModerators() || loggedUser == report.creator)){

            community.updateReport(report,true)
            writeCommunity(community, community.getId())
            DeleteFile.getDeleteFileOperator().deleteReport(report)
        }
    }
}