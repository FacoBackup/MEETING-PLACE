package br.meetingplace.management.services.community.dependencies.followers

import br.meetingplace.data.community.ReportData

interface FollowerInterface {
    fun createReport(data: ReportData)
    fun deleteReport(data: ReportData)
}