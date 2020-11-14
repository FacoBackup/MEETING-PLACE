package br.meetingplace.server.controllers.subjects.services.community.moderators

import br.meetingplace.server.controllers.readwrite.community.CommunityRWInterface
import br.meetingplace.server.controllers.readwrite.group.GroupRWInterface
import br.meetingplace.server.controllers.readwrite.topic.TopicRWInterface
import br.meetingplace.server.controllers.readwrite.user.UserRWInterface
import br.meetingplace.server.dto.MemberOperator
import br.meetingplace.server.dto.community.ApprovalData
import br.meetingplace.server.subjects.services.members.data.MemberType
import br.meetingplace.server.subjects.services.topic.SimplifiedTopic

class Moderator private constructor() {

    companion object {
        private val Class = Moderator()
        fun getClass() = Class
    }

    fun approveTopic(data: ApprovalData, rwCommunity: CommunityRWInterface, rwUser: UserRWInterface, rwTopic: TopicRWInterface) {
        val user = rwUser.read(data.login.email)
        val community = data.identifier.owner?.let { rwCommunity.read(it) }

        if (user != null && community != null && data.login.email in community.getModerators() && !data.identifier.owner.isNullOrBlank()) {
            val topic = rwTopic.read(data.identifier.ID, null)
            if (topic != null) {
                community.updateTopicInValidation(SimplifiedTopic(data.identifier.ID, topic.getOwner()), true)
                rwCommunity.write(community)
            }
        }
    }

    fun approveGroup(data: ApprovalData, rwCommunity: CommunityRWInterface, rwUser: UserRWInterface, rwGroup: GroupRWInterface) {
        val user = rwUser.read(data.login.email)
        val community = data.identifier.owner?.let { rwCommunity.read(it) }

        if (user != null && community != null && (community.getMemberRole(data.login.email) == MemberType.MODERATOR || community.getMemberRole(data.login.email) == MemberType.CREATOR)) {
            val group = rwGroup.read(data.identifier.ID)
            if (group != null) {
                community.updateGroupsInValidation(data.identifier.ID, true)
                rwCommunity.write(community)
            }
        }
    }

    fun stepDown(data: MemberOperator, rwCommunity: CommunityRWInterface, rwUser: UserRWInterface) {
        val user = rwUser.read(data.login.email)
        val community = data.identifier.owner?.let { rwCommunity.read(it) }

        if (user != null && community != null && community.verifyMember(data.login.email))
            community.updateModerator(data.login.email, data.login.email, null)
    }
}