package br.meetingplace.server.controllers.subjects.services.community.reports

import br.meetingplace.server.controllers.readwrite.community.CommunityRWInterface
import br.meetingplace.server.controllers.readwrite.report.ReportRWInterface
import br.meetingplace.server.controllers.readwrite.topic.TopicRWInterface
import br.meetingplace.server.controllers.readwrite.user.UserRWInterface
import br.meetingplace.server.dto.community.ApprovalData
import br.meetingplace.server.dto.community.ReportData
import br.meetingplace.server.subjects.services.community.dependencies.data.Report
import java.util.*

class CommunityReports private constructor() {
    companion object {
        private val Class = CommunityReports()
        fun getClass() = Class
    }

    fun createReport(data: ReportData, rwUser: UserRWInterface, rwCommunity: CommunityRWInterface, rwTopic: TopicRWInterface, rwReport: ReportRWInterface) {
        val user = rwUser.read(data.login.email)
        val community = data.identifier.owner?.let { rwCommunity.read(it) }


        if (user != null && community != null && community.checkTopicApproval(data.identifier.ID)) {
            val topic = rwTopic.read(data.identifier.ID, null)
            if (topic != null) {
                val newReport = Report(
                        UUID.randomUUID().toString(),
                        data.login.email,
                        data.identifier.ID,
                        data.reason,
                        false,
                        community.getID(),
                        null
                )
                community.updateReport(newReport, false)
                rwCommunity.write(community)
                rwReport.write(newReport)
            }
        }
    }

    fun deleteReport(data: ApprovalData, rwUser: UserRWInterface, rwCommunity: CommunityRWInterface, rwReport: ReportRWInterface) {
        val user = rwUser.read(data.login.email)
        val community = data.identifier.owner?.let { rwCommunity.read(it) }
        val report = rwReport.read(data.identifier.ID)

        if (user != null && community != null && report != null && (data.login.email in community.getModerators() || data.login.email == report.creator)) {
            community.updateReport(report, true)
            rwCommunity.write(community)
            rwReport.delete(report)
        }
    }
}