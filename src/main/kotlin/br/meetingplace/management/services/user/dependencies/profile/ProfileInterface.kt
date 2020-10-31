package br.meetingplace.management.services.user.dependencies.profile

import br.meetingplace.data.user.SocialProfileData

interface ProfileInterface {
    fun updateProfile(newProfile: SocialProfileData){}
    fun clearNotifications(){}
}