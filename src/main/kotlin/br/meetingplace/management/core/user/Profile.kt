package br.meetingplace.management.core.user

import br.meetingplace.data.Data
import br.meetingplace.data.user.SocialProfileData
import br.meetingplace.entitie.profiles.SocialProfile
import br.meetingplace.management.core.operators.Verify
import br.meetingplace.management.core.operators.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.management.core.operators.fileOperators.rw.ReadWriteUser
import br.meetingplace.services.notification.Inbox

abstract class Profile: ReadWriteUser, ReadWriteLoggedUser, Verify{

    fun createSocialProfile(newProfile: SocialProfileData){
        val logged = readLoggedUser().email
        val user = readUser(logged)
        if(user.getAge() >=16 && logged != "" && user.getEmail() != ""){
            val social = SocialProfile(newProfile.ProfileName, newProfile.gender, newProfile.nationality, newProfile.about)
            user.createSocialProfile(social)
            writeUser(user, logged)
        }
    } //CREATE

    fun follow(data: Data){
        val logged = readLoggedUser().email
        val user = readUser(logged)
        val external = readUser(data.ID)
        val notification = Inbox("${user.social.getUserName()} is now following you.", "New follower.")

        if(external.getAge() != -1 && verifyLoggedUser(user) && verifyUser(external) && !verifyFollower(external, user)){
            external.social.updateInbox(notification)
            external.social.updateFollowers(logged,false)
            user.social.updateFollowing(data.ID,false)

            writeUser(user, logged)
            writeUser(external ,data.ID)
        }
    } //UPDATE

    fun unfollow(data: Data){
        val logged = readLoggedUser().email
        val user = readUser(logged)
        val external = readUser(data.ID)

        if(external.getAge() != -1 && verifyLoggedUser(user) && verifyUser(external) && verifyFollower(external, user)){
            external.social.updateFollowers(logged,true)
            user.social.updateFollowing(data.ID,true)
            writeUser(user, logged)
            writeUser(external ,data.ID)
        }
    } //UPDATE

    fun clearNotifications(){
        val logged = readLoggedUser().email
        val user = readUser(logged)
        user.social.clearNotifications()
        writeUser(user, logged)
    } //UPDATE
/*
    fun createProfessionalProfile(user: ProfessionalProfile){ // NEEDS WORK

        if(management != -1 && verifyUserSocialProfile(management))
            createProfessionalProfile(user)
    }

 */

}