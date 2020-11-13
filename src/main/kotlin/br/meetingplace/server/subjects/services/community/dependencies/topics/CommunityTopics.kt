package br.meetingplace.server.subjects.services.community.dependencies.topics

import br.meetingplace.server.subjects.services.community.dependencies.data.Report
import br.meetingplace.server.subjects.services.topic.SimplifiedTopic

class CommunityTopics private constructor() : CommunityTopicsInterface {

    companion object {
        private val Class = CommunityTopics()
        fun getClass() = Class
    }

    private val topicsInValidation = mutableListOf<SimplifiedTopic>()
    private val approvedTopics = mutableListOf<SimplifiedTopic>()
    private val reportedTopics = mutableListOf<Report>()
    private val reportIDs = mutableListOf<String>()

    //GETTERS
    override fun getIdTopics() = approvedTopics
    override fun getTopicsInValidation() = topicsInValidation
    override fun getReportedTopics() = reportedTopics
    override fun getIdReports() = reportIDs
    //GETTERS

    override fun checkTopicApproval(id: String): Boolean {
        for (i in 0 until approvedTopics.size) {
            if (approvedTopics[i].ID == id)
                return true
        }
        return false
    }

    override fun removeApprovedTopic(topic: SimplifiedTopic) {
        if (topic in approvedTopics)
            approvedTopics.remove(topic)
    }

    override fun updateTopicInValidation(topic: SimplifiedTopic, approve: Boolean?) {
        when (approve) {
            true -> {//APPROVE
                approvedTopics.add(topic)
                if (topic in topicsInValidation)
                    topicsInValidation.remove(topic)
            }
            false -> { // DELETE
                if (topic in topicsInValidation)
                    topicsInValidation.remove(topic)
            }
            null -> { //ADD
                if (topic !in topicsInValidation)
                    topicsInValidation.add(topic)
            }
        }
    }

    override fun updateReport(data: Report, delete: Boolean) {
        when (delete) {
            true -> {
                if (data.reportId in reportIDs) {
                    reportedTopics.remove(data)
                    reportIDs.remove(data.reportId)
                }
            }
            false -> {
                if (data.reportId !in reportIDs && checkTopicApproval(data.serviceID)
                ) {
                    reportedTopics.add(data)
                    reportIDs.add(data.reportId)
                }
            }
        }
    }
}