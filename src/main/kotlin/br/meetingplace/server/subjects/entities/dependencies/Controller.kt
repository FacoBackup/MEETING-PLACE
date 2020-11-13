package br.meetingplace.server.subjects.entities.dependencies

import br.meetingplace.server.subjects.entities.SimplifiedUser
import br.meetingplace.server.subjects.entities.dependencies.profile.UserProfile
import br.meetingplace.server.subjects.entities.dependencies.profile.UserProfileInterface
import br.meetingplace.server.subjects.entities.dependencies.services.chat.UserChat
import br.meetingplace.server.subjects.entities.dependencies.services.chat.UserChatInterface
import br.meetingplace.server.subjects.entities.dependencies.services.community.UserCommunity
import br.meetingplace.server.subjects.entities.dependencies.services.community.UserCommunityInterface
import br.meetingplace.server.subjects.entities.dependencies.services.follow.UserFollow
import br.meetingplace.server.subjects.entities.dependencies.services.follow.UserFollowInterface
import br.meetingplace.server.subjects.entities.dependencies.services.group.UserGroups
import br.meetingplace.server.subjects.entities.dependencies.services.group.UserGroupsInterface
import br.meetingplace.server.subjects.entities.dependencies.services.notification.UserNotifications
import br.meetingplace.server.subjects.entities.dependencies.services.notification.UserNotificationsInterface
import br.meetingplace.server.subjects.entities.dependencies.services.topic.UserTopics
import br.meetingplace.server.subjects.entities.dependencies.services.topic.UserTopicsInterface
import br.meetingplace.server.subjects.services.chat.SimplifiedChat
import br.meetingplace.server.subjects.services.notification.NotificationData
import br.meetingplace.server.subjects.services.topic.SimplifiedTopic


abstract class Controller : UserChatInterface, UserCommunityInterface, UserFollowInterface, UserGroupsInterface,
        UserTopicsInterface, UserProfileInterface, UserNotificationsInterface {
    private val profile = UserProfile.getClass()
    private val follow = UserFollow.getClass()
    private val notifications = UserNotifications.getClass()
    private val chat = UserChat.getClass()
    private val threads = UserTopics.getClass()
    private val groups = UserGroups.getClass()
    private val communities = UserCommunity.getClass()

    //PROFILE

    override fun getGender(): String? {
        return profile.getGender()
    }

    override fun getNationality(): String? {
        return profile.getNationality()
    }

    override fun getAbout(): String? {
        return profile.getAbout()
    }

    override fun updateProfile(about: String?, nationality: String?, gender: String?) {
        profile.updateProfile(about, nationality, gender)
    }

    override fun updateMyChats(chat: SimplifiedChat) {
        this.chat.updateMyChats(chat)
    }

    //NOTIFICATIONS
    override fun updateInbox(notification: NotificationData) {
        notifications.updateInbox(notification)
    }

    override fun clearNotifications() {
        notifications.clearNotifications()
    }

    //COMMUNITIES
    override fun updateModeratorIn(id: String, leave: Boolean) {
        communities.updateModeratorIn(id, leave)
    }

    override fun updateCommunitiesIFollow(id: String, unfollow: Boolean) {
        communities.updateCommunitiesIFollow(id, unfollow)
    }

    override fun getModeratorIn(): List<String> {
        return communities.getModeratorIn()
    }

    override fun getCommunitiesIFollow(): List<String> {
        return communities.getCommunitiesIFollow()
    }

    //FOLLOW
    override fun getFollowers(): List<String> {
        return follow.getFollowers()
    }

    override fun getFollowing(): List<String> {
        return follow.getFollowing()
    }

    //CHAT
    override fun getMyChats(): List<SimplifiedChat> {
        return chat.getMyChats()
    }

    //THREADS
    override fun getMyTopics(): List<String> {
        return threads.getMyTopics()
    }

    override fun updateMyTopics(topic: String, add: Boolean) {
        threads.updateMyTopics(topic, add)
    }


    override fun updateFollowers(data: String, remove: Boolean) {
        follow.updateFollowers(data, remove)
    }

    override fun updateFollowing(data: String, remove: Boolean) {
        follow.updateFollowing(data, remove)
    }

    //GROUPS
    override fun updateMemberIn(group: String, leave: Boolean) {
        groups.updateMemberIn(group, leave)
    }

    override fun getMyGroups(): List<String> {
        return groups.getMyGroups()
    }

    override fun getMemberIn(): List<String> {
        return groups.getMemberIn()
    }

    override fun updateMyGroups(group: String, delete: Boolean) {
        groups.updateMyGroups(group, delete)
    }

}