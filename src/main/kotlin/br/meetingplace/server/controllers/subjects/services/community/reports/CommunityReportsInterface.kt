package br.meetingplace.server.controllers.subjects.services.community.reports

import br.meetingplace.server.dto.community.ApprovalData
import br.meetingplace.server.dto.community.ReportData

interface CommunityReportsInterface {
    fun createReport(data: ReportData)
    fun deleteReport(data: ApprovalData)
}