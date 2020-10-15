package br.meetingplace.entities.user

import br.meetingplace.entities.user.profiles.ProfessionalProfile
import br.meetingplace.entities.user.profiles.SocialProfile

open class User(
    private var realName: String, // only can create a professional profile if age > 18 and social if age > 16
    private var age: Int,
    private var email: String,
    private var password: String){

    private var id = -1

    var social =  SocialProfile("", "", "", "") //initializes as a null profile
    val professional = ProfessionalProfile()

    //Setters
    fun startUser (new: Int){
        if(id == -1)
            id = new
    }

    fun createSocialProfile(profile: SocialProfile){ // overrides the null profile
        if(age >= 16 && id != -1)
            social = profile
    }

    fun professionalProfile(profile: ProfessionalProfile){
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
        email = new
    }

    fun changePass(oldPass: String, newPass: String){ //NEEDS WORK HERE
        if(oldPass == password && newPass != password)
            password = newPass
    }
    //Update

    //Getters
    fun getId() = id

    fun getPassword() = password

    fun getEmail() = email
    //Getters
}
