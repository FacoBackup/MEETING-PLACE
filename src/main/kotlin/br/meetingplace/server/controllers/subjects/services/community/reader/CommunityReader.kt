package br.meetingplace.server.controllers.subjects.services.community.reader

import br.meetingplace.server.controllers.dependencies.readwrite.community.CommunityRWInterface
import br.meetingplace.server.controllers.dependencies.readwrite.topic.main.TopicRWInterface
import br.meetingplace.server.controllers.dependencies.readwrite.user.UserRWInterface
import br.meetingplace.server.dto.SimpleOperator
import br.meetingplace.server.subjects.services.community.dependencies.data.Report
import br.meetingplace.server.subjects.services.groups.Group
import br.meetingplace.server.subjects.services.members.data.MemberType
import br.meetingplace.server.subjects.services.topic.SimplifiedTopic
import br.meetingplace.server.subjects.services.topic.Topic

class CommunityReader {

    fun seeReports(data: SimpleOperator, rwCommunity: CommunityRWInterface, rwUser: UserRWInterface): List<Report> {
        val user = rwUser.read(data.login.email)
        val community = data.identifier.owner?.let { rwCommunity.read(it) }

        if (user != null && community != null &&
                (community.getMemberRole(data.login.email) == MemberType.MODERATOR || community.getMemberRole(data.login.email) == MemberType.CREATOR))
            return community.getReportedTopics()

        return listOf()
    }

    fun seeMembers(data: SimpleOperator, rwCommunity: CommunityRWInterface, rwUser: UserRWInterface): List<String> {
        val user = rwUser.read(data.login.email)
        val community = data.identifier.owner?.let { rwCommunity.read(it) }

        if (user != null && community != null)
            return community.getMembers()

        return listOf()
    }

    fun seeThreads(data: SimpleOperator, rwTopic: TopicRWInterface, rwCommunity: CommunityRWInterface, rwUser: UserRWInterface): List<Topic> {
        val user = rwUser.read(data.login.email)
        val community = data.identifier.owner?.let { rwCommunity.read(it) }
        lateinit var topics: List<SimplifiedTopic>

        if (user != null && community != null) {
            topics = community.getIdTopics()

            val communityTopics = mutableListOf<Topic>()

            for (element in topics) {
                val topic = rwTopic.read(element.ID)
                if (topic != null)
                    communityTopics.add(topic)
            }

            return communityTopics
        }

        return listOf()
    }

    fun seeGroups(data: SimpleOperator): List<Group> {
//        val logged = rw.readLoggedUser().email
//        val user = rw.readUser(logged)
//        val community = rw.readCommunity(iDs.getCommunityId(data.ID))
//
//        if (verify.verifyUser(user) && verify.verifyCommunity(community)){
//            val groupIDs = community.getApprovedGroups()
//            //for (i in groupIDs.indices)
//
//        }
//        return listOf()

        TODO("NOT IMPLEMENTED YET")
    }
}