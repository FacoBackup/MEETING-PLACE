package br.meetingplace.management.user.dependencies

import br.meetingplace.data.Data
import br.meetingplace.data.user.SocialProfileData
import br.meetingplace.entitie.profiles.SocialProfile
import br.meetingplace.management.dependencies.IDs
import br.meetingplace.management.dependencies.Verify
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteCommunity
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteUser
import br.meetingplace.services.community.Community
import br.meetingplace.services.notification.Inbox

abstract class Profile: ReadWriteUser, ReadWriteLoggedUser, ReadWriteCommunity,Verify, IDs{
    private fun verifyType(data: Data): Int{
        val isCommunity = readCommunity(data.ID)
        val isUser = readUser(data.ID)

        return if(isCommunity.getId() == "" && isUser.getEmail() != "") //IS A USER
            0
        else if(isCommunity.getId() != "" && isUser.getEmail() == "")// IS A COMMUNITY
            1
        else // IS NONE OF THE ABOVE
            -1
    }

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
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)

        when(verifyType(data)){
            0->{ //USER
                val external = readUser(data.ID)
                val notification = Inbox("${user.social.getUserName()} is now following you.", "New follower.")
                if(external.getAge() != -1 && verifyLoggedUser(user) && verifyUser(external) && !verifyFollower(external, user)){
                    external.social.updateInbox(notification)
                    external.social.updateFollowers(loggedUser,false)
                    user.social.updateFollowing(data.ID,false)

                    writeUser(user, loggedUser)
                    writeUser(external ,data.ID)
                }
            }
            1->{ //COMMUNITY
                val community = readCommunity(getCommunityId(data.ID))
                if (verifyLoggedUser(user) && verifyCommunity(community) && community.getId() !in user.social.getCommunitiesIFollow() && loggedUser !in community.getFollowers() && loggedUser !in community.getModerators()){
                    user.social.updateCommunitiesIFollow(community.getId(), false)
                    community.updateFollower(loggedUser, false)
                    writeUser(user, loggedUser)
                    writeCommunity(community, community.getId())
                }
            }
        }

    } //UPDATE

    fun unfollow(data: Data){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)

        when(verifyType(data)){
            0->{ //USER
                val external = readUser(data.ID)
                if(external.getAge() != -1 && verifyLoggedUser(user) && verifyUser(external) && verifyFollower(external, user)){
                    external.social.updateFollowers(loggedUser,true)
                    user.social.updateFollowing(data.ID,true)
                    writeUser(user, loggedUser)
                    writeUser(external ,data.ID)
                }
            }
            1->{ //COMMUNITY
                val community = readCommunity(getCommunityId(data.ID))
                if (verifyLoggedUser(user) && verifyCommunity(community) && community.getId() in user.social.getCommunitiesIFollow() && loggedUser in community.getFollowers() && loggedUser !in community.getModerators()){
                    user.social.updateCommunitiesIFollow(data.ID, true)
                    community.updateFollower(loggedUser, true)
                    writeUser(user, loggedUser)
                    writeCommunity(community, community.getId())
                }

            }
        }
    } //UPDATE

    fun clearNotifications(){
        val logged = readLoggedUser().email
        val user = readUser(logged)
        user.social.clearNotifications()
        writeUser(user, logged)
    } //UPDATE

    fun communitiesIFollow(): List<Community>{
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val communities = mutableListOf<Community>()

        if (verifyLoggedUser(user)){
            val communityList = user.social.getCommunitiesIFollow()
            for(i in 0 until communityList.size){
                val data = readCommunity(communityList[i])
                if(verifyCommunity(data))
                    communities.add(data)
            }
        }
        return communities
    }

    fun moderatorIn(): List<Community>{
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val communities = mutableListOf<Community>()

        if (verifyLoggedUser(user)){
            val communityList = user.social.getModeratorIn()
            for(i in 0 until communityList.size){
                val data = readCommunity(communityList[i])
                if(verifyCommunity(data))
                    communities.add(data)
            }
        }
        return communities
    }

/*
    fun createProfessionalProfile(user: ProfessionalProfile){ // NEEDS WORK

        if(management != -1 && verifyUserSocialProfile(management))
            createProfessionalProfile(user)
    }

 */

}