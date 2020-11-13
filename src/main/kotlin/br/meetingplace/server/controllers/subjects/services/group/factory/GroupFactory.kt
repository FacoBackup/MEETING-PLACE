package br.meetingplace.server.controllers.subjects.services.group.factory

import br.meetingplace.server.controllers.dependencies.readwrite.chat.ChatRWInterface
import br.meetingplace.server.controllers.dependencies.readwrite.community.CommunityRWInterface
import br.meetingplace.server.controllers.dependencies.readwrite.group.GroupRWInterface
import br.meetingplace.server.controllers.dependencies.readwrite.user.UserRWInterface
import br.meetingplace.server.dto.CreationData
import br.meetingplace.server.subjects.services.chat.Chat
import br.meetingplace.server.subjects.services.groups.Group
import br.meetingplace.server.subjects.services.notification.NotificationData
import br.meetingplace.server.subjects.services.owner.OwnerType
import br.meetingplace.server.subjects.services.owner.chat.ChatOwnerData
import br.meetingplace.server.subjects.services.owner.group.GroupOwnerData

class GroupFactory private constructor() {
    companion object {
        private val Class = GroupFactory()
        fun getClass() = Class
    }
    private fun getGroupID(name: String, creator: String): String{
            return (name.replace("\\s".toRegex(), "") + "_" + (creator.replaceAfter("@", "")).removeSuffix("@")).toLowerCase()
    }

    fun create(data: CreationData, rwGroup: GroupRWInterface, rwUser: UserRWInterface, rwCommunity: CommunityRWInterface, rwChat: ChatRWInterface) {
        val user = rwUser.read(data.login.email)
        lateinit var communityMods: List<String>
        lateinit var notification: NotificationData
        lateinit var newGroup: Group
        val groupID = getGroupID(data.name, data.login.email)

        if (user != null && data.name.isNotEmpty() && groupID !in user.getMemberIn() && groupID !in user.getMyGroups()) {
            when (data.identifier.community) {
                false -> {
                    val newChat = Chat(groupID + "_CHAT", ChatOwnerData(user.getEmail(), groupID, OwnerType.USER, OwnerType.GROUP))
                    newGroup = Group(GroupOwnerData(data.login.email, data.login.email, OwnerType.USER), groupID, data.name, newChat.getID())
                    user.updateMyGroups(groupID, false)

                    rwGroup.write(newGroup)
                    rwChat.write(newChat)
                    rwUser.write(user)
                }
                true -> {
                    val community = rwCommunity.read(data.identifier.ID)
                    if (community != null ) {
                        communityMods = community.getModerators()
                        notification = NotificationData("${data.login.email} wants to create a new group in ${community.getName()}.", "Community Group")
                        for (moderator in communityMods) {
                            val mod = rwUser.read(moderator)
                            if (mod != null && mod != user)
                                mod.updateInbox(notification)
                        }
                        val newChat = Chat(groupID + "_CHAT", ChatOwnerData(community.getID(), groupID, OwnerType.COMMUNITY, OwnerType.GROUP))
                        newGroup = Group(GroupOwnerData(community.getID(), data.login.email, OwnerType.COMMUNITY), groupID, data.name, newChat.getID())

                        user.updateMyGroups(groupID, false)

                        if (data.login.email !in community.getModerators())
                            community.updateGroupsInValidation(newGroup.getGroupID(), null)
                        else if (data.login.email in community.getModerators() || data.login.email in community.getCreator())
                            community.updateGroupsInValidation(newGroup.getGroupID(), true)


                        rwGroup.write(newGroup)
                        rwChat.write(newChat)
                        rwUser.write(user)
                        rwCommunity.write(community)
                    }
                }
            }
        }
    }

}