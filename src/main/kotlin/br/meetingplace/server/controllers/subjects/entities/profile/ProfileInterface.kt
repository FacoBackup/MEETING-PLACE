package br.meetingplace.server.controllers.subjects.entities.profile

import br.meetingplace.server.controllers.readwrite.user.UserRWInterface
import br.meetingplace.server.dto.Login
import br.meetingplace.server.dto.user.ProfileData

interface ProfileInterface {
    fun updateProfile(newProfile: ProfileData, rwUser: UserRWInterface)
    fun clearNotifications(data: Login, rwUser: UserRWInterface)
}