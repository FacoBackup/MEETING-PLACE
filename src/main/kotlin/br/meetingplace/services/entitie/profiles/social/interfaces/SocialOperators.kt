package br.meetingplace.services.entitie.profiles.social.interfaces

import br.meetingplace.services.notification.Inbox

interface SocialOperators {

    fun createSocialProfile(userName: String, about: String, nationality: String, gender: String)

    fun updateAbout(newAbout: String)
    fun updateInbox(notification: Inbox)
    fun getUserName(): String?
}