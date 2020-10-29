package br.meetingplace.services.entitie

import br.meetingplace.services.entitie.profiles.ProfessionalProfile
import br.meetingplace.services.entitie.profiles.SocialProfile

open class User(
    private var realName: String, // only can create a professional profile if age > 18 and social if age > 16
    private var age: Int,
    private var email: String,
    private var password: String){

    var social =  SocialProfile("", "", "", "") //initializes as a null profile
    val professional = ProfessionalProfile()

    //Setters
    fun createSocialProfile(profile: SocialProfile){ // overrides the null profile
        if(age >= 16 && email != "" && social.getUserName() == "")
            social = profile
    }
/*
    fun professionalProfile(profile: ProfessionalProfile){
        if(age >= 18 && email != ""){
                       professional.userName = profile.userName
            professional.about = profile.about
            professional.gender = profile.gender
            professional.nationality = profile.nationality

        }
    }
 */

    //Setters

    //Update
    fun changePass(oldPass: String, newPass: String){ //NEEDS WORK HERE
        if(oldPass == password && newPass != password)
            password = newPass
    }
    //Update

    //Getters
    fun getPassword() = password
    fun getAge() = age
    fun getEmail() = email
    //Getters
}