package br.meetingplace.management.services.user.dependencies.follow

import br.meetingplace.data.Data
import br.meetingplace.management.dependencies.IDs
import br.meetingplace.management.dependencies.Verify
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteCommunity
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteThread
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteUser
import br.meetingplace.services.notification.Inbox

class FollowOperator private constructor(): FollowInterface, Verify, ReadWriteUser, ReadWriteThread, ReadWriteCommunity, IDs {

    companion object{
        private val Class = FollowOperator()
        fun getFollowOperator() = Class
    }

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

    override fun follow(data: Data){
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

    override fun unfollow(data: Data){
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
}