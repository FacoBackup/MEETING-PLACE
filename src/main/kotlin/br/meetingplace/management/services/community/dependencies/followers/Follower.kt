package br.meetingplace.management.services.community.dependencies.followers

import br.meetingplace.data.community.ApprovalData
import br.meetingplace.data.community.ReportData
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.verify.dependencies.Verify
import br.meetingplace.management.dependencies.ReadWriteController
import br.meetingplace.management.dependencies.readwrite.dependencies.community.ReadWriteCommunity
import br.meetingplace.management.dependencies.readwrite.dependencies.community.ReadWriteReport
import br.meetingplace.management.dependencies.readwrite.dependencies.thread.ReadWriteThread
import br.meetingplace.management.dependencies.readwrite.dependencies.user.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.readwrite.dependencies.user.ReadWriteUser
import br.meetingplace.services.community.data.Report

class Follower private constructor():FollowerInterface, ReadWriteCommunity, ReadWriteUser, ReadWriteLoggedUser, ReadWriteThread, Verify, ReadWriteReport {
    private val iDs = IDsController.getClass()
    companion object{
        private val Class = Follower()
        fun getClass () = Class
    }
    override fun createReport(data: ReportData){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val community = readCommunity(iDs.getCommunityId(data.communityId))
        val thread = readThread(data.idService)

        if (verifyLoggedUser(user) && verifyCommunity(community) && verifyThread(thread)){
            //the verify community method already insures that the id and name are different of null so don't mind the !!
            val newReport = Report(iDs.generateId(), loggedUser, data.idService, data.reason, false, community.getId()!!, null)
            community.updateReport(newReport, false)
            writeCommunity(community, community.getId()!!)
            writeReport(newReport, newReport.reportId)
        }
    }

    override fun deleteReport(data: ApprovalData) {
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val community = readCommunity(iDs.getCommunityId(data.community))
        val report = readReport(data.id)

        if (verifyLoggedUser(user) && verifyCommunity(community) && verifyReport(report) && (loggedUser in community.getModerators() || loggedUser == report.creator)){
            community.updateReport(report,true)
            //the verify community method already insures that the id and name are different of null so don't mind the !!
            writeCommunity(community, community.getId()!!)
            ReadWriteController.getDeleteFileOperator().deleteReport(report)
        }
    }
}