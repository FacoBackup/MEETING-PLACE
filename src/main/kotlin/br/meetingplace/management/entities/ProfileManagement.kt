package br.meetingplace.management.entities

import br.meetingplace.data.entities.user.Follower
import br.meetingplace.data.entities.group.UserMember
import br.meetingplace.data.startup.SocialProfileData
import br.meetingplace.entities.user.profiles.SocialProfile
import br.meetingplace.management.GeneralManagement
import br.meetingplace.management.operations.ReadWrite.ReadWrite
import br.meetingplace.management.operations.verifiers.UserVerifiers
import br.meetingplace.servicies.notification.Inbox
import java.io.File

open class ProfileManagement: GroupManagement(){

    private val verifier = UserVerifiers.getUserVerifier()
    private val rw = ReadWrite.getRW()
    private val system = GeneralManagement.getManagement()
    private val management = system.getLoggedUser()


    fun createSocialProfile(newProfile: SocialProfileData){
        val fileUser = File("$management.json").exists()


        if(management != "" && fileUser){
            val user = rw.readUser(management)
            val social = SocialProfile(newProfile.ProfileName, newProfile.gender, newProfile.nationality, newProfile.about)
            user.createSocialProfile(social)
            rw.writeUser(management,user)
        }
    }

/*
    fun createProfessionalProfile(user: ProfessionalProfile){ // NEEDS WORK

        if(management != -1 && verifyUserSocialProfile(management))
            createProfessionalProfile(user)
    }

 */

    fun follow(data: Follower){

        val fileUser = File("$management.json").exists()
        val fileExternal = File("${data.external}.json").exists()
        if(management != "" && fileUser && fileExternal){
            val user = rw.readUser(management)
            val external = rw.readUser(data.external)
            val notification = Inbox("${user.social.getUserName()} is now following you.", "New follower.")

            if(verifier.verifyFollower(data)){ // verifies if the user you want to follow exists
                external.social.updateInbox(notification)
                external.social.followers.add(management)
                user.social.following.add(data.external)

                rw.writeUser(management,user)
                rw.writeUser(data.external,external)
            }
        }
    }

    fun unfollow(data: Follower){

        val fileUser = File("$management.json").exists()
        val fileExternal = File("${data.external}.json").exists()

        if(management != "" && fileUser && fileExternal){

            val user = rw.readUser(management)
            val external = rw.readUser(data.external)

            user.social.following.remove(data.external)
            external.social.followers.remove(management)

            rw.writeUser(management,user)
            rw.writeUser(data.external,external)
        }
    }
/*
    fun leaveGroup(member: UserMember){

        if(management != ""){

            val indexGroup = getGroupIndex(member.group)

            if(indexGroup != -1 && groupList[indexGroup].getCreator() != logged)
                removeMember(member)
        }
    }

 */


}