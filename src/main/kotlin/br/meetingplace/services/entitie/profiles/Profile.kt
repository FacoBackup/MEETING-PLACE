package br.meetingplace.services.entitie.profiles

import br.meetingplace.services.entitie.profiles.professional.ProfessionalProfile
import br.meetingplace.services.entitie.profiles.social.SocialProfile
import br.meetingplace.services.entitie.profiles.social.interfaces.*
import br.meetingplace.services.notification.Inbox


abstract class Profile: SocialChats, SocialCommunities, SocialFollowers, SocialGroups, SocialThreads, SocialOperators {
    private val social = SocialProfile.getClass()
    private val professional = ProfessionalProfile.getClass()

    override fun createSocialProfile(userName: String, about: String, nationality: String, gender: String){
        social.createSocialProfile(userName, about, nationality, gender)
    }

    override fun updateMyChats(id: String) {
        social.updateMyChats(id)
    }

    override fun getUserName(): String? {
        return social.getUserName()
    }

    override fun updateAbout(newAbout: String) {
        social.updateAbout(newAbout)
    }

    override fun updateInbox(notification: Inbox) {
        social.updateInbox(notification)
    }

    override fun clearNotifications() {
        social.clearNotifications()
    }

    override fun getCommunitiesIFollow(): List<String> {
        return social.getCommunitiesIFollow()
    }

    override fun getFollowers(): List<String> {
        return social.getFollowers()
    }

    override fun getFollowing(): List<String> {
        return social.getFollowing()
    }

    override fun getMemberIn(): List<String> {
        return social.getMemberIn()
    }

    override fun getModeratorIn(): List<String> {
        return social.getModeratorIn()
    }

    override fun getMyChats(): List<String> {
        return social.getMyChats()
    }

    override fun getMyGroups(): List<String> {
        return social.getMyGroups()
    }

    override fun getMyThreads(): List<String> {
        return social.getMyThreads()
    }

    override fun updateCommunitiesIFollow(id: String, unfollow: Boolean) {
        social.updateCommunitiesIFollow(id,unfollow)
    }

    override fun updateFollowers(userEmail: String, remove: Boolean) {
        social.updateFollowers(userEmail,remove)
    }

    override fun updateFollowing(userEmail: String, remove: Boolean) {
        social.updateFollowing(userEmail,remove)
    }

    override fun updateMemberIn(idGroup: String, leave: Boolean) {
        social.updateMemberIn(idGroup,leave)
    }

    override fun updateModeratorIn(id: String, leave: Boolean) {
        social.updateModeratorIn(id, leave)
    }

    override fun updateMyGroups(idGroup: String, delete: Boolean) {
        social.updateMyGroups(idGroup,delete)
    }

    override fun updateMyThreads(id: String, add: Boolean) {
        social.updateMyThreads(id, add)
    }

}