package br.meetingplace.server.controllers.subjects.services.community.reports

import br.meetingplace.server.controllers.dependencies.id.controller.IDController
import br.meetingplace.server.controllers.dependencies.rw.controller.RWController
import br.meetingplace.server.controllers.dependencies.verify.controller.VerifyController
import br.meetingplace.server.dto.community.ApprovalData
import br.meetingplace.server.dto.community.ReportData
import br.meetingplace.server.subjects.services.community.dependencies.data.Report

class CommunityReports private constructor() : CommunityReportsInterface {

    private val iDs = IDController.getClass()
    private val rw = RWController.getClass()
    private val verify = VerifyController.getClass()

    companion object {
        private val Class = CommunityReports()
        fun getClass() = Class
    }

    override fun createReport(data: ReportData) {
        val user = rw.readUser(data.login.email)
        val community = data.identifier.owner?.let { rw.readCommunity(it) }


        if (verify.verifyUser(user) && community != null && verify.verifyCommunity(community) && community.checkTopicApproval(data.identifier.ID)) {
            val topic = rw.readTopic(data.identifier.ID, user.getEmail(), null, true)
            if (verify.verifyTopic(topic)) {
                val newReport = Report(
                        iDs.generateId(),
                        data.login.email,
                        data.identifier.ID,
                        data.reason,
                        false,
                        community.getID(),
                        null
                )
                community.updateReport(newReport, false)
                rw.writeCommunity(community, community.getID())
                rw.writeReport(newReport)
            }
        }
    }

    override fun deleteReport(data: ApprovalData) {
        val user = rw.readUser(data.login.email)
        val community = data.identifier.owner?.let { rw.readCommunity(it) }
        val report = rw.readReport(data.identifier.ID)

        if (verify.verifyUser(user) && community != null && verify.verifyCommunity(community) && verify.verifyReport(report) && (data.login.email in community.getModerators() || data.login.email == report.creator)) {
            community.updateReport(report, true)
            rw.writeCommunity(community, community.getID())
            rw.deleteReport(report)
        }
    }
}