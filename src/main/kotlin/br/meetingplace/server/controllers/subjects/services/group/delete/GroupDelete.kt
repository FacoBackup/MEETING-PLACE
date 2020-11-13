package br.meetingplace.server.controllers.subjects.services.group.delete

import br.meetingplace.server.controllers.dependencies.id.controller.IDController
import br.meetingplace.server.controllers.dependencies.rw.controller.RWController
import br.meetingplace.server.controllers.dependencies.verify.controller.VerifyController
import br.meetingplace.server.dto.SimpleOperator
import br.meetingplace.server.subjects.services.community.Community
import br.meetingplace.server.subjects.services.groups.Group

class GroupDelete private constructor() : GroupDeleteInterface {
    private val iDs = IDController.getClass()
    private val rw = RWController.getClass()
    private val verify = VerifyController.getClass()

    companion object {
        private val Class = GroupDelete()
        fun getClass() = Class
    }

    override fun delete(data: SimpleOperator) {
        val user = rw.readUser(data.login.email)

        lateinit var community: Community
        lateinit var members: List<String>
        lateinit var group: Group

        if (verify.verifyUser(user) && !data.identifier.owner.isNullOrBlank()) {
            when (data.identifier.community) {
                false -> {
                    group = rw.readGroup(data.identifier.ID, data.identifier.owner, false)

                    if (verify.verifyGroup(group) && data.login.email == group.getCreator()) {
                        members = group.getMembers()
                        for (element in members) {
                            val member = rw.readUser(element)
                            if (verify.verifyUser(member)) {
                                member.updateMemberIn(group.getGroupID(), true)
                                rw.writeUser(member, member.getEmail())
                            }
                        }
                        user.updateMyGroups(group.getGroupID(), true)
                        val chat = rw.readChat("", group.getCreator(), group.getGroupID(), group = true, community = false)
                        rw.deleteChat(chat)
                        rw.deleteGroup(group)
                        rw.writeUser(user, user.getEmail())
                    }
                }
                true -> {
                    group = rw.readGroup(data.identifier.ID, data.identifier.owner, true)
                    community = rw.readCommunity(iDs.getCommunityId(data.identifier.owner))

                    if (verify.verifyGroup(group) && verify.verifyCommunity(community) && data.login.email == group.getCreator()) {
                        when (community.checkGroupApproval(group.getGroupID())) {
                            true -> {
                                members = group.getMembers()
                                for (element in members) {
                                    val member = rw.readUser(element)
                                    if (verify.verifyUser(member)) {
                                        member.updateMemberIn(group.getGroupID(), true)
                                        rw.writeUser(member, member.getEmail())
                                    }
                                }

                                community.removeApprovedGroup(group.getGroupID())
                                user.updateMyGroups(group.getGroupID(), true)
                                rw.deleteGroup(group)
                                rw.writeUser(user, user.getEmail())
                                rw.writeCommunity(community, community.getID())
                            }
                            false -> {
                                members = group.getMembers()
                                for (element in members) {
                                    val member = rw.readUser(element)
                                    if (verify.verifyUser(member)) {
                                        member.updateMemberIn(group.getGroupID(), true)
                                        rw.writeUser(member, member.getEmail())
                                    }
                                }

                                community.updateGroupsInValidation(group.getGroupID(), false)
                                user.updateMyGroups(group.getGroupID(), true)
                                val chat = rw.readChat("", community.getID(), group.getGroupID(), group = true, community = true)
                                rw.deleteChat(chat)
                                rw.deleteGroup(group)
                                rw.writeUser(user, user.getEmail())
                                rw.writeCommunity(community, community.getID())
                            }//false
                        }//when
                    }//if
                }//false
            }//when
        }//if
    }//method
}