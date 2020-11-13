package br.meetingplace.server.controllers.subjects.services.community.factory

import br.meetingplace.server.controllers.dependencies.id.controller.IDController
import br.meetingplace.server.controllers.dependencies.rw.controller.RWController
import br.meetingplace.server.controllers.dependencies.verify.controller.VerifyController
import br.meetingplace.server.dto.CreationData
import br.meetingplace.server.dto.MemberOperator
import br.meetingplace.server.subjects.services.community.Community
import br.meetingplace.server.subjects.services.notification.NotificationData

class CommunityFactory private constructor() {

    private val iDs = IDController.getClass()
    private val rw = RWController.getClass()
    private val verify = VerifyController.getClass()

    companion object {
        private val Class = CommunityFactory()
        fun getClass() = Class
    }

    fun create(data: CreationData) {
        val user = rw.readUser(data.login.email)
        val community = rw.readCommunity(iDs.getCommunityId(data.name))

        lateinit var newCommunity: Community
        lateinit var id: String

        if (verify.verifyUser(user) && !verify.verifyCommunity(community)) {
            newCommunity = Community(data.name, iDs.getCommunityId(data.name), data.about, user.getEmail())
            id = iDs.getCommunityId(data.name)
            user.updateModeratorIn(id, false)
            rw.writeCommunity(newCommunity, id)
            rw.writeUser(user, user.getEmail())
        }
    }

    fun delete(data: MemberOperator) {
        val user = rw.readUser(data.login.email)
        lateinit var community: Community
        lateinit var notification: NotificationData
        lateinit var mods: List<String>

        if (verify.verifyUser(user)) {
            community = rw.readCommunity(data.identifier.ID)
            if (verify.verifyCommunity(community)) {
                when (community.getModerators().isEmpty()) {
                    true -> rw.deleteCommunity(community)
                    false -> {
                        notification = NotificationData(
                                "${user.getUserName()} requested for a community deletion, if you approve the deletion you have to step-down from moderator.",
                                "Community"
                        )
                        mods = community.getModerators()
                        for (i in mods.indices) {
                            val moderator = rw.readUser(mods[i])
                            if (verify.verifyUser(moderator)) {
                                moderator.updateInbox(notification)
                            }
                        }
                    }
                }
            }
        }
    }
}