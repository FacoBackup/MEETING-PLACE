package br.meetingplace.server.controllers.subjects.services.community.moderators

import br.meetingplace.server.controllers.dependencies.rw.controller.RWController
import br.meetingplace.server.controllers.dependencies.verify.controller.VerifyController
import br.meetingplace.server.dto.MemberOperator
import br.meetingplace.server.dto.community.ApprovalData
import br.meetingplace.server.subjects.services.members.data.MemberType
import br.meetingplace.server.subjects.services.topic.SimplifiedTopic

class Moderator private constructor() : ModeratorInterface {
    private val rw = RWController.getClass()
    private val verify = VerifyController.getClass()

    companion object {
        private val Class = Moderator()
        fun getClass() = Class
    }

    override fun approveTopic(data: ApprovalData) {
        val user = rw.readUser(data.login.email)
        val community = data.identifier.owner?.let { rw.readCommunity(it) }

        if (verify.verifyUser(user) && community != null && verify.verifyCommunity(community) && data.login.email in community.getModerators() && !data.identifier.owner.isNullOrBlank()) {
            val topic = rw.readTopic(data.identifier.ID, data.identifier.owner, null, true)
            if (verify.verifyTopic(topic)) {
                community.updateTopicInValidation(SimplifiedTopic(data.identifier.ID, topic.getOwner()), true)
                rw.writeCommunity(community, community.getID())
            }
        }
    }

    override fun approveGroup(data: ApprovalData) {
        val user = rw.readUser(data.login.email)
        val community = data.identifier.owner?.let { rw.readCommunity(it) }

        if (verify.verifyUser(user) && community != null && verify.verifyCommunity(community) && (community.getMemberRole(
                        data.login.email
                ) == MemberType.MODERATOR || community.getMemberRole(data.login.email) == MemberType.CREATOR)
        ) {
            val group = rw.readGroup(data.identifier.ID, data.identifier.owner, true)
            if (verify.verifyGroup(group)) {
                community.updateGroupsInValidation(data.identifier.ID, true)
                rw.writeCommunity(community, community.getID())
            }
        }
    }

    override fun stepDown(data: MemberOperator) {
        val user = rw.readUser(data.login.email)
        val community = data.identifier.owner?.let { rw.readCommunity(it) }

        if (verify.verifyUser(user) && community != null && verify.verifyCommunity(community) && community.verifyMember(data.login.email))
            community.updateModerator(data.login.email, data.login.email, null)
    }
}