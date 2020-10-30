package br.meetingplace.management.services.user.dependencies.follow

import br.meetingplace.data.Data
import br.meetingplace.management.dependencies.IDs
import br.meetingplace.management.dependencies.Verify
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteCommunity
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteThread
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteUser
import br.meetingplace.services.entitie.User
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
        lateinit var notification: Inbox
        lateinit var external: User

        if(verifyLoggedUser(user)){
            when(verifyType(data)){
                0->{ //USER
                    external = readUser(data.ID)
                    notification = Inbox("${user.getUserName()} is now following you.", "New follower.")

                    if(verifyLoggedUser(user) && verifyUser(external) && !verifyFollower(external, user)){
                        external.updateInbox(notification)
                        external.updateFollowers(loggedUser,false)
                        user.updateFollowing(data.ID,false)

                        writeUser(user, loggedUser)
                        writeUser(external ,data.ID)
                    }
                }
                1->{ //COMMUNITY
                    val community = readCommunity(getCommunityId(data.ID))

                    if (verifyLoggedUser(user) && verifyCommunity(community)  &&loggedUser !in community.getFollowers() && community.getId() !in user.getCommunitiesIFollow() && loggedUser !in community.getModerators()){

                        user.updateCommunitiesIFollow(community.getId(), false)
                        community.updateFollower(loggedUser, false)

                        writeUser(user, loggedUser)
                        writeCommunity(community, community.getId())
                    }
                }
            }

        }
    } //UPDATE

    override fun unfollow(data: Data){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)

        if(verifyLoggedUser(user)){
            when(verifyType(data)){
                0->{ //USER
                    val external = readUser(data.ID)
                    if(external.getAge() != -1 && verifyLoggedUser(user) && verifyUser(external) && verifyFollower(external, user)){

                        external.updateFollowers(loggedUser,true)
                        user.updateFollowing(data.ID,true)
                        writeUser(user, loggedUser)
                        writeUser(external ,data.ID)
                    }
                }
                1->{ //COMMUNITY
                    val community = readCommunity(getCommunityId(data.ID))
                    if (verifyLoggedUser(user) && verifyCommunity(community)){

                        when(community.getId() in user.getCommunitiesIFollow() && loggedUser in community.getFollowers()){
                            true->{
                                user.updateCommunitiesIFollow(data.ID, true)
                                community.updateFollower(loggedUser, true)
                            }
                        }
                        when(loggedUser in community.getModerators() && community.getId() in user.getModeratorIn()){
                            true-> community.updateModerator(loggedUser, loggedUser, true)
                        }

                        writeUser(user, loggedUser)
                        writeCommunity(community, community.getId())
                    }
                }
            }
        }
    } //UPDATE
}