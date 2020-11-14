package br.meetingplace.server.controllers.subjects.entities.profile

import br.meetingplace.server.controllers.readwrite.user.UserRWInterface
import br.meetingplace.server.dto.Login
import br.meetingplace.server.dto.user.ProfileData

class Profile private constructor() : ProfileInterface {

    companion object {
        private val Class = Profile()
        fun getClass() = Class
    }

    override fun updateProfile(newProfile: ProfileData, rwUser: UserRWInterface) {
        val user = rwUser.read(newProfile.login.email)

        if (user != null) {
            user.updateProfile(
                    if (!newProfile.about.isNullOrBlank()) newProfile.about else user.getAbout(),
                    if (!newProfile.nationality.isNullOrBlank()) newProfile.nationality else user.getNationality(),
                    if (!newProfile.gender.isNullOrBlank()) newProfile.gender else user.getGender()
            )
            rwUser.write(user)
        }
    }

    override fun clearNotifications(data: Login, rwUser: UserRWInterface) {
        val user = rwUser.read(data.email)
        if(user != null){
            user.clearNotifications()
            rwUser.write(user)
        }
    }
}