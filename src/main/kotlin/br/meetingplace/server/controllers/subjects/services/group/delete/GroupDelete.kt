package br.meetingplace.server.controllers.subjects.services.group.delete

import br.meetingplace.server.controllers.readwrite.chat.ChatRWInterface
import br.meetingplace.server.controllers.readwrite.community.CommunityRWInterface
import br.meetingplace.server.controllers.readwrite.group.GroupRWInterface
import br.meetingplace.server.controllers.readwrite.user.UserRWInterface
import br.meetingplace.server.dto.SimpleOperator

class GroupDelete private constructor() {
    companion object {
        private val Class = GroupDelete()
        fun getClass() = Class
    }

    fun delete(data: SimpleOperator, rwCommunity: CommunityRWInterface, rwUser: UserRWInterface, rwChat: ChatRWInterface, rwGroup: GroupRWInterface) {
        val user = rwUser.read(data.login.email)
        val group = rwGroup.read(data.identifier.ID)

        lateinit var members: List<String>

        if (user != null && group != null && data.login.email == group.getCreator() && !data.identifier.owner.isNullOrBlank()) {
            val chat = rwChat.read(group.getChatID())
            when (data.identifier.community) {
                false -> { // USER
                        members = group.getMembers()
                        for (element in members) {
                            val member = rwUser.read(element)
                            if (member != null) {
                                member.updateMemberIn(group.getGroupID(), true)
                                rwUser.write(member)
                            }
                        }
                        user.updateMyGroups(group.getGroupID(), true)

                        if(chat != null)
                            rwChat.delete(chat)
                        rwGroup.delete(group)
                        rwUser.write(user)
                }
                true -> { // COMMUNITY
                    val community = rwCommunity.read(data.identifier.owner)

                    if (community != null) {
                        when (community.checkGroupApproval(group.getGroupID())) {
                            true -> { // APPROVED

                                members = group.getMembers()
                                for (element in members) {
                                    val member = rwUser.read(element)
                                    if (member != null) {
                                        member.updateMemberIn(group.getGroupID(), true)
                                        rwUser.write(member)
                                    }
                                }

                                community.removeApprovedGroup(group.getGroupID())
                                user.updateMyGroups(group.getGroupID(), true)
                                rwGroup.delete(group)
                                rwUser.write(user)
                                rwCommunity.write(community)
                            }
                            false -> { //IN VALIDATION

                                members = group.getMembers()
                                for (element in members) {
                                    val member = rwUser.read(element)
                                    if (member != null) {
                                        member.updateMemberIn(group.getGroupID(), true)
                                        rwUser.write(member)
                                    }
                                }

                                community.updateGroupsInValidation(group.getGroupID(), false)
                                user.updateMyGroups(group.getGroupID(), true)

                                if(chat != null)
                                    rwChat.delete(chat)
                                rwGroup.delete(group)
                                rwUser.write(user)
                                rwCommunity.write(community)
                            }//false
                        }//when
                    }//if
                }//false
            }//when
        }//if
    }//method
}