package br.meetingplace.management.entities

import br.meetingplace.data.entities.user.Follower
import br.meetingplace.data.startup.SocialProfileData
import br.meetingplace.entities.user.profiles.SocialProfile
import br.meetingplace.interfaces.ReadFile
import br.meetingplace.interfaces.Refresh
import br.meetingplace.interfaces.WriteFile
import br.meetingplace.management.operations.verifiers.UserVerifiers
import br.meetingplace.servicies.notification.Inbox
import java.io.File

class ProfileManagement private constructor(): ReadFile, WriteFile, Refresh{

    private val verifier = UserVerifiers.getUserVerifier()

    companion object{
        private val management = ProfileManagement()
        fun getManagement () = management
    }
    fun createSocialProfile(newProfile: SocialProfileData){
        val log = refreshData()
        val management = log.user
        val fileUser = File("$management.json").exists()


        if(management != "" && fileUser){
            val user = readUser(management)
            val social = SocialProfile(newProfile.ProfileName, newProfile.gender, newProfile.nationality, newProfile.about)
            user.createSocialProfile(social)
            writeUser(management,user)
        }
    }

/*
    fun createProfessionalProfile(user: ProfessionalProfile){ // NEEDS WORK

        if(management != -1 && verifyUserSocialProfile(management))
            createProfessionalProfile(user)
    }

 */

    fun follow(data: Follower){
        val log = refreshData()
        val management = log.user
        val fileUser = File("$management.json").exists()
        val fileExternal = File("${data.external}.json").exists()
        if(management != "" && fileUser && fileExternal){
            val user = readUser(management)
            val external = readUser(data.external)
            val notification = Inbox("${user.social.getUserName()} is now following you.", "New follower.")

            if(verifier.verifyFollower(data)){ // verifies if the user you want to follow exists
                external.social.updateInbox(notification)
                external.social.followers.add(management)
                user.social.following.add(data.external)

                writeUser(management,user)
                writeUser(data.external,external)
            }
        }
    }

    fun unfollow(data: Follower){
        val log = refreshData()
        val management = log.user
        val fileUser = File("$management.json").exists()
        val fileExternal = File("${data.external}.json").exists()

        if(management != "" && fileUser && fileExternal){

            val user = readUser(management)
            val external = readUser(data.external)

            user.social.following.remove(data.external)
            external.social.followers.remove(management)

            writeUser(management,user)
            writeUser(data.external,external)
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