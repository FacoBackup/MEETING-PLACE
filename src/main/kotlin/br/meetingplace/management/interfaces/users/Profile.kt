package br.meetingplace.management.interfaces.users

import br.meetingplace.data.user.Follower
import br.meetingplace.data.user.SocialProfileData
import br.meetingplace.entitie.profiles.SocialProfile
import br.meetingplace.interfaces.file.ReadFile
import br.meetingplace.interfaces.file.WriteFile
import br.meetingplace.interfaces.utility.Path
import br.meetingplace.interfaces.utility.Refresh
import br.meetingplace.interfaces.utility.Verifiers
import br.meetingplace.servicies.notification.Inbox

interface Profile: ReadFile, WriteFile, Refresh, Path, Verifiers {

    fun createSocialProfile(newProfile: SocialProfileData){
        val management = readLoggedUser().email

        if(management != "" && verifyPath("users",management)){
            val user = readUser(management)
            val social = SocialProfile(newProfile.ProfileName, newProfile.gender, newProfile.nationality, newProfile.about)
            user.createSocialProfile(social)
            writeUser(management,user)
        }
    } //CREATE
    fun follow(data: Follower){
        val management = readLoggedUser().email

        if(management != "" && verifyPath("users",management) && verifyPath("users",data.external)){
            println("Starting")
            val user = readUser(management)
            val external = readUser(data.external)
            val notification = Inbox("${user.social.getUserName()} is now following you.", "New follower.")
            if(!verifyFollower(data)){ // verifies if the user you want to follow exists
                println("step2")
                external.social.updateInbox(notification)
                external.social.updateFollowers(management,false)
                user.social.updateFollowing(data.external,false)
                writeUser(management,user)
                writeUser(data.external,external)
            }
        }
    } //UPDATE
    fun unfollow(data: Follower){
        val management = readLoggedUser().email

        if(management != "" && verifyPath("users",management) && verifyPath("users",data.external)){

            val user = readUser(management)
            val external = readUser(data.external)

            user.social.updateFollowing(data.external,true)
            external.social.updateFollowers(management,true)

            writeUser(management,user)
            writeUser(data.external,external)
        }
    } //UPDATE
/*
    fun leaveGroup(member: UserMember){

        if(management != ""){

            val indexGroup = getGroupIndex(member.group)

            if(indexGroup != -1 && groupList[indexGroup].getCreator() != logged)
                removeMember(member)
        }
    }

 */
/*
    fun createProfessionalProfile(user: ProfessionalProfile){ // NEEDS WORK

        if(management != -1 && verifyUserSocialProfile(management))
            createProfessionalProfile(user)
    }

 */
}