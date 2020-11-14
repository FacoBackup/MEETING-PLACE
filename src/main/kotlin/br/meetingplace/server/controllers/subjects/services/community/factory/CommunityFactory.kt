package br.meetingplace.server.controllers.subjects.services.community.factory

import br.meetingplace.server.controllers.readwrite.community.CommunityRWInterface
import br.meetingplace.server.controllers.readwrite.user.UserRWInterface
import br.meetingplace.server.dto.CreationData
import br.meetingplace.server.dto.MemberOperator
import br.meetingplace.server.subjects.services.community.Community
import br.meetingplace.server.subjects.services.notification.NotificationData

class CommunityFactory private constructor() {

    companion object {
        private val Class = CommunityFactory()
        fun getClass() = Class
    }

    private fun getCommunityID(name: String): String{
        return (name.replace("\\s".toRegex(), "")).toLowerCase()
    }

    fun create(data: CreationData, rwUser: UserRWInterface, rwCommunity: CommunityRWInterface) {
        val user = rwUser.read(data.login.email)
        val community = rwCommunity.read(data.name)

        lateinit var newCommunity: Community
        lateinit var id: String

        if (user != null && community == null) {
            newCommunity = Community(data.name, getCommunityID(data.name), data.about, user.getEmail())
            id = getCommunityID(data.name)
            user.updateModeratorIn(id, false)
            rwCommunity.write(newCommunity)
            rwUser.write(user)
        }
    }

    fun delete(data: MemberOperator, rwUser: UserRWInterface, rwCommunity: CommunityRWInterface) {
        val user = rwUser.read(data.login.email)
        lateinit var notification: NotificationData
        lateinit var mods: List<String>

        if (user != null) {
            val community = rwCommunity.read(data.identifier.ID)
            if (community != null) {
                when (community.getModerators().isEmpty()) {
                    true -> rwCommunity.delete(community)
                    false -> {
                        notification = NotificationData(
                                "${user.getUserName()} requested for a community deletion, if you approve the deletion you have to step-down from moderator.",
                                "Community"
                        )
                        mods = community.getModerators()
                        for (i in mods.indices) {
                            val moderator = rwUser.read(mods[i])
                            moderator?.updateInbox(notification)
                        }
                    }
                }
            }
        }
    }
}