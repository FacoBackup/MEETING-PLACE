package br.meetingplace.management.services.community.dependencies.followers

import br.meetingplace.data.community.ApprovalData
import br.meetingplace.data.community.ReportData
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.readwrite.controller.ReadWriteController
import br.meetingplace.management.dependencies.verify.controller.VerifyController
import br.meetingplace.services.community.data.Report

class Follower private constructor():FollowerInterface{
    private val iDs = IDsController.getClass()
    private val rw = ReadWriteController.getClass()
    private val verify = VerifyController.getClass()
    companion object{
        private val Class = Follower()
        fun getClass () = Class
    }
    override fun createReport(data: ReportData){
        val logged = rw.readLoggedUser().fileName
        val user = rw.readUser(if(!logged.isNullOrBlank()) logged else "")

        val community = rw.readCommunity(iDs.getCommunityId(data.communityId))
        val thread = rw.readThread(data.idService)

        if (verify.verifyUser(user) && verify.verifyCommunity(community) && verify.verifyThread(thread)){
            //the verify community method already insures that the id and name are different of null so don't mind the !!
            val newReport = Report(iDs.generateId(), logged, data.idService, data.reason, false, community.getId()!!, null)
            community.updateReport(newReport, false)
            rw.writeCommunity(community, community.getId()!!)
            rw.writeReport(newReport, newReport.reportId)
        }
    }

    override fun deleteReport(data: ApprovalData) {
        val logged = rw.readLoggedUser().fileName
        val user = rw.readUser(if(!logged.isNullOrBlank()) logged else "")

        val community = rw.readCommunity(iDs.getCommunityId(data.community))
        val report = rw.readReport(data.id)

        if (verify.verifyUser(user) && verify.verifyCommunity(community) && verify.verifyReport(report) && (logged in community.getModerators() || logged == report.creator)){
            community.updateReport(report,true)
            //the verify community method already insures that the id and name are different of null so don't mind the !!
            rw.writeCommunity(community, community.getId()!!)
            rw.deleteReport(report)
        }
    }
}