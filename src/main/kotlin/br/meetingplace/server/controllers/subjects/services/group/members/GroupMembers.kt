package br.meetingplace.server.controllers.subjects.services.group.members

import br.meetingplace.server.controllers.readwrite.community.CommunityRWInterface
import br.meetingplace.server.controllers.readwrite.group.GroupRWInterface
import br.meetingplace.server.controllers.readwrite.user.UserRWInterface
import br.meetingplace.server.dto.MemberOperator
import br.meetingplace.server.subjects.services.members.data.MemberData
import br.meetingplace.server.subjects.services.members.data.MemberType
import br.meetingplace.server.subjects.services.notification.NotificationData
import br.meetingplace.server.subjects.services.notification.data.NotificationMainType
import br.meetingplace.server.subjects.services.notification.data.NotificationSubType

class GroupMembers private constructor() {
    companion object {
        private val Class = GroupMembers()
        fun getClass() = Class
    }

    fun addMember(data: MemberOperator, rwCommunity: CommunityRWInterface, rwGroup: GroupRWInterface, rwUser: UserRWInterface) {
        val user = rwUser.read(data.login.email)
        val newMember = rwUser.read(data.memberEmail)

        lateinit var notification: NotificationData
        lateinit var toBeAdded: MemberData

        if (user != null && newMember != null && !data.identifier.owner.isNullOrBlank()) {
            toBeAdded = MemberData(newMember.getEmail(), MemberType.NORMAL)
            when (data.identifier.community) {
                false -> {
                    val group = rwGroup.read(data.identifier.ID)
                    if (group != null && group.verifyMember(data.login.email)
                            && !group.verifyMember(data.memberEmail)) {

                        notification = NotificationData(NotificationMainType.GROUP, NotificationSubType.ADDED_IN_GROUP)

                        group.updateMember(toBeAdded, false)
                        newMember.updateMemberIn(group.getGroupID(), false)
                        newMember.updateInbox(notification)
                        rwUser.write(newMember)
                        rwGroup.write(group)
                    }
                }
                true -> {
                    val group = rwGroup.read(data.identifier.ID)
                    val community = rwCommunity.read(data.identifier.owner)
                    if (group != null && community != null && (data.login.email == group.getCreator() || data.login.email in community.getModerators())) {
                        notification = NotificationData(NotificationMainType.GROUP, NotificationSubType.ADDED_IN_GROUP)
                        group.updateMember(toBeAdded, false)
                        newMember.updateMemberIn(group.getGroupID(), false)
                        newMember.updateInbox(notification)
                        rwUser.write(newMember)
                        rwGroup.write(group)
                    }
                }
            }
        }
    }

    fun changeMemberRole(data: MemberOperator, rwGroup: GroupRWInterface, rwUser: UserRWInterface) {
        val user = rwUser.read(data.login.email)
        val member = rwUser.read(data.memberEmail)

        val group = when (data.identifier.community) {
            false -> data.identifier.owner?.let { rwGroup.read(data.identifier.ID) }
            true -> data.identifier.owner?.let { rwGroup.read(data.identifier.ID) }
        }

        if (user != null && group != null && member != null &&
                (group.getMemberRole(user.getEmail()) == MemberType.MODERATOR || group.getMemberRole(user.getEmail()) == MemberType.CREATOR) &&
                group.getMemberRole(member.getEmail()) != null
        ) {

            when (data.stepDown) {
                true -> {
                    if (group.getMemberRole(member.getEmail()) == MemberType.MODERATOR)
                        group.updateMemberRole(member.getEmail(), MemberType.NORMAL)
                }
                false -> {
                    if (group.getMemberRole(member.getEmail()) == MemberType.NORMAL)
                        group.updateMemberRole(member.getEmail(), MemberType.MODERATOR)
                }
            }
        }
    }

    fun removeMember(data: MemberOperator, rwGroup: GroupRWInterface, rwUser: UserRWInterface, rwCommunity: CommunityRWInterface) {

        val user = rwUser.read(data.login.email)
        val member = rwUser.read(data.memberEmail)

        lateinit var toBeRemoved: MemberData

        if (user != null && member != null && !data.identifier.owner.isNullOrBlank()) {
            when (data.identifier.community) {
                false -> {
                    val group = rwGroup.read(data.identifier.ID)


                    if (group != null && group.verifyMember(data.login.email) && (data.login.email in group.getModerators() || data.login.email in group.getCreator())) {
                        val memberRole = group.getMemberRole(member.getEmail())
                        if(memberRole != null){
                            toBeRemoved = MemberData(member.getEmail(), memberRole)
                            group.updateMember(toBeRemoved, true)
                            member.updateMemberIn(group.getGroupID(), true)

                            rwUser.write(member)
                            rwGroup.write(group)
                        }

                    }

                }
                true -> { //COMMUNITY
                    val group = rwGroup.read(data.identifier.ID)
                    val community = rwCommunity.read(data.identifier.owner)
                    if (group != null && community != null && (data.login.email == group.getCreator() || data.login.email in community.getModerators())) {

                        group.updateMember(toBeRemoved, true)
                        member.updateMemberIn(group.getGroupID(), true)
                        rwUser.write(member)
                        rwGroup.write(group)
                    }
                }
            }
        }
    } //UPDATE
}