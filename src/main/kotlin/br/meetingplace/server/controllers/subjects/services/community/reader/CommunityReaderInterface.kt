package br.meetingplace.server.controllers.subjects.services.community.reader

import br.meetingplace.server.dto.SimpleOperator
import br.meetingplace.server.subjects.services.community.dependencies.data.Report
import br.meetingplace.server.subjects.services.groups.Group
import br.meetingplace.server.subjects.services.topic.Topic

interface CommunityReaderInterface {
    fun seeReports(data: SimpleOperator): List<Report>
    fun seeMembers(data: SimpleOperator): List<String>
    fun seeThreads(data: SimpleOperator): List<Topic>
    fun seeGroups(data: SimpleOperator): List<Group>
}