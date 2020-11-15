package br.meetingplace.server.controllers.subjects.entities.social

import br.meetingplace.server.controllers.readwrite.community.CommunityRWInterface
import br.meetingplace.server.controllers.readwrite.user.UserRWInterface

import br.meetingplace.server.dto.SimpleOperator
import br.meetingplace.server.subjects.services.notification.NotificationData
import br.meetingplace.server.subjects.services.notification.data.NotificationMainType
import br.meetingplace.server.subjects.services.notification.data.NotificationSubType

class Social private constructor() : SocialInterface {

    companion object {
        private val Class = Social()
        fun getClass() = Class
    }

    override fun follow(data: SimpleOperator, rwUser: UserRWInterface, rwCommunity: CommunityRWInterface) {
        val user = rwUser.read(data.login.email)
        lateinit var notification: NotificationData


        if (user != null) {
            when (data.identifier.community) {
                false -> { //USER
                    val external = rwUser.read(data.identifier.ID)
                    notification = NotificationData(NotificationMainType.USER, NotificationSubType.FOLLOWING)
                    if (external != null && user.getEmail() !in external.getFollowers()) {


                        external.updateInbox(notification)
                        external.updateFollowers(user.getEmail(), false)
                        user.updateFollowing(external.getEmail(), false)

                        rwUser.write(user)
                        rwUser.write(external)
                    }
                }
                true -> { //COMMUNITY
                    val community = rwCommunity.read(data.identifier.ID)

                    if (community != null && !community.verifyMember(data.login.email)) {
                        user.updateCommunitiesIFollow(community.getID(), false)
                        community.updateFollower(data.identifier.ID, false)
                        rwUser.write(user)
                        rwCommunity.write(community)
                    }
                }
            }

        }
    } //UPDATE

    override fun unfollow(data: SimpleOperator, rwUser: UserRWInterface, rwCommunity: CommunityRWInterface) {
        val user = rwUser.read(data.login.email)

        if (user != null) {
            when (data.identifier.community) {
                false -> { //USER
                    val external = rwUser.read(data.identifier.ID)
                    if (external != null && user.getEmail() !in external.getFollowers()) {

                        external.updateFollowers(user.getEmail(), true)
                        user.updateFollowing(external.getEmail(), true)

                        rwUser.write(user)
                        rwUser.write(external)
                    }
                }
                true -> { //COMMUNITY
                    val community = rwCommunity.read(data.identifier.ID)

                    if (community != null && !community.verifyMember(data.login.email)) {
                        user.updateCommunitiesIFollow(community.getID(), true)
                        community.updateFollower(data.identifier.ID, true)
                        rwUser.write(user)
                        rwCommunity.write(community)
                    }
                }
            }
        }
    }
}