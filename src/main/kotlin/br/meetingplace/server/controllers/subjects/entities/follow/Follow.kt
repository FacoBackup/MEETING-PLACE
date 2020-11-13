package br.meetingplace.server.controllers.subjects.entities.follow

import br.meetingplace.server.controllers.dependencies.newRW.community.CommunityRWInterface
import br.meetingplace.server.controllers.dependencies.newRW.user.UserRWInterface

import br.meetingplace.server.dto.SimpleOperator
import br.meetingplace.server.subjects.entities.SimplifiedUser
import br.meetingplace.server.subjects.entities.User
import br.meetingplace.server.subjects.services.notification.NotificationData

class Follow private constructor() : FollowInterface {

    companion object {
        private val Class = Follow()
        fun getClass() = Class
    }

    override fun follow(data: SimpleOperator, rwUser: UserRWInterface, rwCommunity: CommunityRWInterface) {
        val user = rwUser.read(data.login.email)
        lateinit var notification: NotificationData
        lateinit var external: User


        if (user.getEmail().isNotBlank()) {
            when (data.identifier.community) {
                false -> { //USER
                    external = rwUser.read(data.identifier.ID)
                    notification = NotificationData("${user.getUserName()} is now following you.", "New follower.")
                    if (external.getEmail().isNotBlank() && user.getEmail() !in external.getFollowers()) {


                        external.updateInbox(notification)
                        external.updateFollowers(user.getEmail(), false)
                        user.updateFollowing(external.getEmail(), false)

                        rwUser.write(user)
                        rwUser.write(external)
                    }
                }
                true -> { //COMMUNITY
                    val community = rwCommunity.read(data.identifier.ID)
                    if (community.getID().isNotBlank() && community.verifyMember(data.login.email)) {

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

        lateinit var external: User
        if (user.getEmail().isNotBlank()) {
            when (data.identifier.community) {
                false -> { //USER
                    external = rwUser.read(data.identifier.ID)
                    if (external.getAge() != -1 && external.getEmail().isNotBlank() && user.getEmail() in external.getFollowers()) {

                        external.updateFollowers(user.getEmail(), true)
                        user.updateFollowing(external.getEmail(), true)

                        rwUser.write(user)
                        rwUser.write(external)
                    }
                }
                true -> { //COMMUNITY
                    val community = rwCommunity.read(data.identifier.ID)

                    if (community.getID().isNotBlank() && community.verifyMember(data.login.email)) {

                        user.updateCommunitiesIFollow(data.identifier.ID, true)
                        community.updateFollower(data.identifier.ID, true)
                        rwUser.write(user)
                        rwCommunity.write(community)
                    }
                }
            }
        }
    }
}