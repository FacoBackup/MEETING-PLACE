package br.meetingplace.server.controllers.subjects.services.community.reader

import br.meetingplace.server.controllers.dependencies.id.controller.IDController
import br.meetingplace.server.controllers.dependencies.rw.controller.RWController
import br.meetingplace.server.controllers.dependencies.verify.controller.VerifyController
import br.meetingplace.server.dto.SimpleOperator
import br.meetingplace.server.subjects.services.community.dependencies.data.Report
import br.meetingplace.server.subjects.services.groups.Group
import br.meetingplace.server.subjects.services.members.data.MemberType
import br.meetingplace.server.subjects.services.topic.SimplifiedTopic
import br.meetingplace.server.subjects.services.topic.Topic

class CommunityReader : CommunityReaderInterface {
    private val rw = RWController.getClass()
    private val verify = VerifyController.getClass()
    private val iDs = IDController.getClass()

    override fun seeReports(data: SimpleOperator): List<Report> {
        val user = rw.readUser(data.login.email)
        val community = data.identifier.owner?.let { rw.readCommunity(it) }

        if (verify.verifyUser(user) && community != null && verify.verifyCommunity(community) && (community.getMemberRole(data.login.email) == MemberType.MODERATOR || community.getMemberRole(
                        data.login.email
                ) == MemberType.CREATOR)
        )
            return community.getReportedTopics()

        return listOf()
    }

    override fun seeMembers(data: SimpleOperator): List<String> {
        val user = rw.readUser(data.login.email)
        val community = data.identifier.owner?.let { rw.readCommunity(it) }

        if (verify.verifyUser(user) && community != null && verify.verifyCommunity(community))
            return community.getMembers()

        return listOf()
    }

    override fun seeThreads(data: SimpleOperator): List<Topic> {
        val user = rw.readUser(data.login.email)
        val community = data.identifier.owner?.let { rw.readCommunity(it) }
        lateinit var topics: List<SimplifiedTopic>

        if (verify.verifyUser(user) && community != null && verify.verifyCommunity(community)) {
            topics = community.getIdTopics()

            val communityTopics = mutableListOf<Topic>()

            for (element in topics) {
                val topic = rw.readTopic(element.ID, element.owner.mainTopicOwnerID, null, false)
                if (verify.verifyTopic(topic))
                    communityTopics.add(topic)
            }

            return communityTopics
        }

        return listOf()
    }

    override fun seeGroups(data: SimpleOperator): List<Group> {
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