package br.meetingplace.server.controllers.subjects.services.group.members

import br.meetingplace.server.controllers.dependencies.id.controller.IDController
import br.meetingplace.server.controllers.dependencies.rw.controller.RWController
import br.meetingplace.server.controllers.dependencies.verify.controller.VerifyController
import br.meetingplace.server.dto.MemberOperator
import br.meetingplace.server.subjects.services.community.Community
import br.meetingplace.server.subjects.services.groups.Group
import br.meetingplace.server.subjects.services.members.data.MemberData
import br.meetingplace.server.subjects.services.members.data.MemberType
import br.meetingplace.server.subjects.services.notification.NotificationData

class GroupMembers private constructor() : GroupMembersInterface {
    private val iDs = IDController.getClass()
    private val rw = RWController.getClass()
    private val verify = VerifyController.getClass()

    companion object {
        private val Class = GroupMembers()
        fun getClass() = Class
    }

    override fun addMember(data: MemberOperator) {
        val user = rw.readUser(data.login.email)
        val newMember = rw.readUser(data.memberEmail)

        lateinit var community: Community
        lateinit var notification: NotificationData
        lateinit var toBeAdded: MemberData

        if (verify.verifyUser(user) && verify.verifyUser(newMember) && !data.identifier.owner.isNullOrBlank()) {
            toBeAdded = MemberData(newMember.getEmail(), MemberType.NORMAL)
            when (data.identifier.community) {
                false -> {
                    val group = rw.readGroup(data.identifier.ID, data.identifier.owner, false)
                    if (verify.verifyGroup(group) && verify.verifyUser(newMember) && group.verifyMember(data.login.email) && !group.verifyMember(
                                    data.memberEmail
                            )
                    ) {
                        notification = NotificationData("You're now a member of ${group.getNameGroup()}", "Group.")

                        group.updateMember(toBeAdded, false)
                        newMember.updateMemberIn(group.getGroupID(), false)
                        newMember.updateInbox(notification)
                        rw.writeUser(newMember, newMember.getEmail())
                        rw.writeGroup(group)
                    }
                }
                true -> {
                    val group = rw.readGroup(data.identifier.ID, data.identifier.owner, true)
                    community = rw.readCommunity(iDs.getCommunityId(data.identifier.owner))
                    if (verify.verifyGroup(group) && verify.verifyCommunity(community) && (data.login.email == group.getCreator() || data.login.email in community.getModerators())) {
                        notification = NotificationData("You're now a member of ${group.getNameGroup()}", "Group.")
                        group.updateMember(toBeAdded, false)
                        newMember.updateMemberIn(group.getGroupID(), false)
                        newMember.updateInbox(notification)
                        rw.writeUser(newMember, newMember.getEmail())
                        rw.writeGroup(group)
                    }
                }
            }
        }
    }

    override fun changeMemberRole(data: MemberOperator) {
        val user = rw.readUser(data.login.email)
        val member = rw.readUser(data.memberEmail)

        val group = when (data.identifier.community) {
            false -> data.identifier.owner?.let { rw.readGroup(data.identifier.ID, it, false) }
            true -> data.identifier.owner?.let { rw.readGroup(data.identifier.ID, it, true) }
        }

        if (verify.verifyUser(user) && group != null && verify.verifyGroup(group) && verify.verifyUser(member) &&
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

    override fun removeMember(data: MemberOperator) {

        val user = rw.readUser(data.login.email)
        val member = rw.readUser(data.memberEmail)

        lateinit var toBeRemoved: MemberData
        lateinit var community: Community
        lateinit var group: Group

        if (verify.verifyUser(user) && verify.verifyUser(member) && !data.identifier.owner.isNullOrBlank()) {
            when (data.identifier.community) {
                false -> {

                    group = rw.readGroup(data.identifier.ID, data.identifier.owner, false)
                    val memberRole = group.getMemberRole(member.getEmail())

                    if (verify.verifyGroup(group) && group.verifyMember(data.login.email) && memberRole != null && (data.login.email in group.getModerators() || data.login.email in group.getCreator())) {
                        toBeRemoved = MemberData(member.getEmail(), memberRole)
                        group.updateMember(toBeRemoved, true)
                        member.updateMemberIn(group.getGroupID(), true)

                        rw.writeUser(member, member.getEmail())
                        rw.writeGroup(group)
                    }

                }
                true -> { //COMMUNITY
                    group = rw.readGroup(data.identifier.ID, data.identifier.owner, true)
                    community = rw.readCommunity(iDs.getCommunityId(data.identifier.owner))
                    if (verify.verifyGroup(group) && verify.verifyCommunity(community) && (data.login.email == group.getCreator() || data.login.email in community.getModerators())) {

                        group.updateMember(toBeRemoved, true)
                        member.updateMemberIn(group.getGroupID(), true)
                        rw.writeUser(member, member.getEmail())
                        rw.writeGroup(group)
                    }
                }
            }
        }
    } //UPDATE
}