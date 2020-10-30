package br.meetingplace.management.services.user.dependencies.profile

import br.meetingplace.data.user.SocialProfileData
import br.meetingplace.management.dependencies.Verify
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteUser

class Profile private constructor(): ProfileInterface, ReadWriteUser, ReadWriteLoggedUser, Verify{

    companion object{
        private val Class = Profile()
        fun getClass() = Class
    }

    override fun createProfile(newProfile: SocialProfileData){
        val logged = readLoggedUser().email
        val user = readUser(logged)
        if(user.getAge() >=16 && logged != "" && user.getEmail() != ""){
            user.createSocialProfile(newProfile.ProfileName, newProfile.gender, newProfile.nationality, newProfile.about)
            writeUser(user, logged)
        }
    } //CREATE
    override fun clearNotifications(){
        val logged = readLoggedUser().email
        val user = readUser(logged)
        user.clearNotifications()
        writeUser(user, logged)
    } //UPDATE


/*
    fun createProfessionalProfile(user: ProfessionalProfile){ // NEEDS WORK

        if(management != -1 && verifyUserSocialProfile(management))
            createProfessionalProfile(user)
    }

 */

}