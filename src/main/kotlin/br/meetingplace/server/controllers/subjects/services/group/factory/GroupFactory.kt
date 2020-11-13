package br.meetingplace.server.controllers.subjects.services.group.factory


import br.meetingplace.server.controllers.dependencies.id.controller.IDController
import br.meetingplace.server.controllers.dependencies.rw.controller.RWController
import br.meetingplace.server.controllers.dependencies.verify.controller.VerifyController
import br.meetingplace.server.dto.CreationData
import br.meetingplace.server.subjects.services.chat.Chat
import br.meetingplace.server.subjects.services.groups.Group
import br.meetingplace.server.subjects.services.notification.NotificationData
import br.meetingplace.server.subjects.services.owner.OwnerType
import br.meetingplace.server.subjects.services.owner.chat.ChatOwnerData
import br.meetingplace.server.subjects.services.owner.group.GroupOwnerData

class GroupFactory private constructor() : GroupFactoryInterface {

    private val iDs = IDController.getClass()
    private val rw = RWController.getClass()
    private val verify = VerifyController.getClass()

    companion object {
        private val Class = GroupFactory()
        fun getClass() = Class
    }

    override fun create(data: CreationData) {
        val user = rw.readUser(data.login.email)
        lateinit var communityMods: List<String>
        lateinit var notification: NotificationData
        lateinit var newGroup: Group
        val groupID = iDs.getGroupId(data.name, data.login.email)

        if (verify.verifyUser(user) && data.name.isNotEmpty() && groupID !in user.getMemberIn() && groupID !in user.getMyGroups()) {
            when (data.identifier.community) {
                false -> {
                    val newChat = Chat(groupID + "_CHAT", ChatOwnerData(user.getEmail(), groupID, OwnerType.USER, OwnerType.GROUP))
                    newGroup = Group(GroupOwnerData(data.login.email, data.login.email, OwnerType.USER), groupID, data.name, newChat.getID())
                    user.updateMyGroups(groupID, false)

                    rw.writeGroup(newGroup)
                    rw.writeChat(newChat)
                    rw.writeUser(user, user.getEmail())
                }
                true -> {
                    val community = data.identifier.owner?.let { rw.readCommunity(it) }
                    if (community != null && verify.verifyCommunity(community)) {
                        communityMods = community.getModerators()
                        notification = NotificationData("${data.login.email} wants to create a new group in ${community.getName()}.", "Community Group")
                        for (moderator in communityMods) {
                            val mod = rw.readUser(moderator)
                            if (verify.verifyUser(mod) && mod != user)
                                mod.updateInbox(notification)
                        }
                        val newChat = Chat(groupID + "_CHAT", ChatOwnerData(community.getID(), groupID, OwnerType.COMMUNITY, OwnerType.GROUP))
                        newGroup = Group(GroupOwnerData(community.getID(), data.login.email, OwnerType.COMMUNITY), groupID, data.name, newChat.getID())

                        user.updateMyGroups(groupID, false)

                        if (data.login.email !in community.getModerators())
                            community.updateGroupsInValidation(newGroup.getGroupID(), null)
                        else if (data.login.email in community.getModerators() || data.login.email in community.getCreator())
                            community.updateGroupsInValidation(newGroup.getGroupID(), true)


                        rw.writeGroup(newGroup)
                        rw.writeChat(newChat)
                        rw.writeUser(user, user.getEmail())
                        rw.writeCommunity(community, community.getID())
                    }
                }
            }
        }
    }

}