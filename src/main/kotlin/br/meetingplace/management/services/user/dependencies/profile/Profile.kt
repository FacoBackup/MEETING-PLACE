package br.meetingplace.management.services.user.dependencies.profile

import br.meetingplace.data.user.SocialProfileData
import br.meetingplace.management.dependencies.verify.dependencies.Verify
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.readwrite.dependencies.user.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.readwrite.dependencies.user.ReadWriteUser

class Profile private constructor(): ProfileInterface, ReadWriteUser, ReadWriteLoggedUser, Verify {
    private val iDs = IDsController.getClass()
    companion object{
        private val Class = Profile()
        fun getClass() = Class
    }

    override fun updateProfile(newProfile: SocialProfileData){
        val logged = readLoggedUser().email
        val user = readUser(logged)
        if(user.getAge() >=16 && verifyLoggedUser(user)){
            user.updateSocialProfile(newProfile.gender, newProfile.nationality, newProfile.about)
            writeUserToFile(user,iDs.attachNameToEmail(user.getUserName(),user.getEmail()))
        }
    } //CREATE

    override fun clearNotifications(){
        val logged = readLoggedUser().email
        val user = readUser(logged)
        user.clearNotifications()
        writeUserToFile(user,iDs.attachNameToEmail(user.getUserName(),user.getEmail()))
    } //UPDATE


/*
    fun createProfessionalProfile(user: ProfessionalProfile){ // NEEDS WORK

        if(management != -1 && verifyUserSocialProfile(management))
            createProfessionalProfile(user)
    }

 */

}