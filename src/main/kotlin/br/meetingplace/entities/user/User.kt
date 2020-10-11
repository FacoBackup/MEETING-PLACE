package br.meetingplace.entities.user

import br.meetingplace.entities.user.profiles.ProfessionalProfile
import br.meetingplace.entities.user.profiles.SocialProfile
import br.meetingplace.servicies.management.GeneralEntitiesManagement

open class User(){


    private var age= -1 // only can create a professional profile if age > 18 and social if age > 16
    private var id = -1
    private var userEmail= ""
    private var pass= ""
    val social = SocialProfile()
    val professional = ProfessionalProfile()

    //Setters
    fun startUser (new: Int){
        if(id == -1)
            id = new
    }

    fun createSocialProfile(profile: SocialProfile){
        if(age >= 16 && id != -1){
            social.userName = profile.userName
            social.about = profile.about
            social.gender = profile.gender
            social.nacionality = profile.nacionality
        }
    }

    fun createProfessionalProfile(profile: ProfessionalProfile){
        if(age >= 18 && id != -1){
            /*
            professional.userName = profile.userName
            professional.about = profile.about
            professional.gender = profile.gender
            professional.nacionality = profile.nacionality
            */
        }
    }
    //Setters

    //Update
    fun changeEmail(new: String) {//NEEDS WORK HERE
        userEmail = new
    }

    fun changePass(oldPass: String, newPass: String){ //NEEDS WORK HERE
        if(oldPass == pass)
            pass = newPass
    }
    //Update

    //Getters
    fun getId() = id

    fun getPass() = pass

    fun getEmail() = userEmail
    //Getters
}
