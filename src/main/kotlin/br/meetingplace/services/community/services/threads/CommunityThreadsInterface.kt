package br.meetingplace.services.community.services.threads

import br.meetingplace.services.community.data.Report

interface CommunityThreadsInterface {
    fun getIdThreads(): List<String>
    fun getThreadsInValidation(): List<String>
    fun getReportedThreads(): List<Report>
    fun getIdReports(): List<String>
    fun checkThreadApproval(id: String): Boolean
    fun removeApprovedThread(thread: String)
    fun updateThreadsInValidation(thread: String, approve: Boolean?)
    fun updateReport(data: Report, delete: Boolean)

}