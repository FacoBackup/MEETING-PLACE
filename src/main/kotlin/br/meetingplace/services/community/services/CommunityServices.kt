package br.meetingplace.services.community.services

import br.meetingplace.services.community.services.groups.CommunityGroups
import br.meetingplace.services.community.services.groups.CommunityGroupsInterface
import br.meetingplace.services.community.services.threads.CommunityThreads
import br.meetingplace.services.community.services.threads.CommunityThreadsInterface
import br.meetingplace.services.community.data.Report

abstract class CommunityServices:CommunityThreadsInterface, CommunityGroupsInterface {

    private val threads = CommunityThreads.getClass()
    private val groups = CommunityGroups.getClass()

    override fun checkGroupApproval(id: String): Boolean {
        return groups.checkGroupApproval(id)
    }

    override fun checkThreadApproval(id: String): Boolean {
        return threads.checkThreadApproval(id)
    }

    override fun getApprovedGroups(): List<String> {
        return groups.getApprovedGroups()
    }

    override fun getGroupsInValidation(): List<String> {
        return groups.getGroupsInValidation()
    }

    override fun getIdReports(): List<String> {
        return threads.getIdReports()
    }

    override fun getIdThreads(): List<String> {
        return threads.getIdThreads()
    }

    override fun getReportedThreads(): List<Report> {
        return threads.getReportedThreads()
    }

    override fun getThreadsInValidation(): List<String> {
        return threads.getThreadsInValidation()
    }

    override fun removeApprovedGroup(group: String) {
        groups.removeApprovedGroup(group)
    }

    override fun removeApprovedThread(thread: String) {
        threads.removeApprovedThread(thread)
    }

    override fun updateGroupsInValidation(group: String, approve: Boolean?) {
        groups.updateGroupsInValidation(group,approve)
    }

    override fun updateReport(data: Report, delete: Boolean) {
        threads.updateReport(data, delete)
    }

    override fun updateThreadsInValidation(thread: String, approve: Boolean?) {
        threads.updateThreadsInValidation(thread, approve)
    }
}