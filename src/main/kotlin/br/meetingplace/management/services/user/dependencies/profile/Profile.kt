package br.meetingplace.management.services.user.dependencies.profile

import br.meetingplace.data.user.SocialProfileData
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.readwrite.controller.ReadWriteController
import br.meetingplace.management.dependencies.verify.controller.VerifyController

class Profile private constructor(): ProfileInterface{
    private val iDs = IDsController.getClass()
    private val rw = ReadWriteController.getClass()
    private val verify = VerifyController.getClass()

    companion object{
        private val Class = Profile()
        fun getClass() = Class
    }

    override fun updateProfile(newProfile: SocialProfileData){
        val logged = rw.readLoggedUser().email
        val user = rw.readUser(logged)
        if(user.getAge() >=16 && verify.verifyUser(user)){
            user.updateSocialProfile(newProfile.gender, newProfile.nationality, newProfile.about)
            rw.writeUserToFile(user,iDs.attachNameToEmail(user.getUserName(),user.getEmail()))
        }
    } //CREATE

    override fun clearNotifications(){
        val logged = rw.readLoggedUser().email
        val user =rw.readUser(logged)
        user.clearNotifications()
        rw.writeUserToFile(user,iDs.attachNameToEmail(user.getUserName(),user.getEmail()))
    } //UPDATE


/*
    fun createProfessionalProfile(user: ProfessionalProfile){ // NEEDS WORK

        if(management != -1 && verifyUserSocialProfile(management))
            createProfessionalProfile(user)
    }

 */

}