package br.meetingplace.management.services.user.dependencies.follow

import br.meetingplace.data.Data
import br.meetingplace.management.dependencies.IDs
import br.meetingplace.management.dependencies.Verify
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteCommunity
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteThread
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteUser
import br.meetingplace.services.entitie.User
import br.meetingplace.services.entitie.profiles.followdata.FollowData
import br.meetingplace.services.notification.Inbox

class Follow private constructor(): FollowInterface, Verify, ReadWriteUser, ReadWriteThread, ReadWriteCommunity, IDs {

    companion object{
        private val Class = Follow()
        fun getClass() = Class
    }

    override fun follow(data: Data){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        lateinit var notification: Inbox
        lateinit var external: User
        lateinit var followingData: FollowData
        lateinit var followerData: FollowData

        if(verifyLoggedUser(user)){
            when(verifyType(data)){
                0->{ //USER
                    external = readUser(data.ID)
                    notification = Inbox("${user.getUserName()} is now following you.", "New follower.")
                    if(verifyLoggedUser(user) && verifyUser(external) && !verifyFollower(external, user)){

                        //the verify user method already insures that the user name and id are different of null, so don't mind the !!
                        followerData = FollowData( user.getUserName()!!,user.getEmail())
                        followingData = FollowData( external.getUserName()!!,external.getEmail())


                        external.updateInbox(notification)
                        external.updateFollowers(followerData,false)
                        user.updateFollowing(followingData,false)

                        writeUser(user, loggedUser)
                        writeUser(external ,data.ID)
                    }
                }
                1->{ //COMMUNITY
                    val community = readCommunity(getCommunityId(data.ID))

                    if (verifyLoggedUser(user) && verifyCommunity(community)  && loggedUser !in community.getFollowers() && loggedUser !in community.getModerators() && community.getId() !in user.getCommunitiesIFollow() && community.getId() !in user.getModeratorIn()){
                        //the verify community method already insures that the id and name are different of null so don't mind the !!
                        user.updateCommunitiesIFollow(community.getId()!!, false)
                        community.updateFollower(loggedUser, false)

                        writeUser(user, loggedUser)
                        writeCommunity(community, community.getId()!!)
                    }
                }
            }

        }
    } //UPDATE

    override fun unfollow(data: Data){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        lateinit var followingData: FollowData
        lateinit var followerData: FollowData
        lateinit var external: User
        if(verifyLoggedUser(user)){
            when(verifyType(data)){
                0->{ //USER
                    external = readUser(data.ID)
                    if(external.getAge() != -1 && verifyLoggedUser(user) && verifyUser(external) && verifyFollower(external, user)){

                        //the verify user method already insures that the user name and id are different of null, so don't mind the !!
                        followerData = FollowData( user.getUserName()!!,user.getEmail())
                        followingData = FollowData( external.getUserName()!!,external.getEmail())

                        external.updateFollowers(followerData,true)
                        user.updateFollowing(followingData,true)
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
                        //the verify community method already insures that the id and name are different of null so don't mind the !!
                        writeUser(user, loggedUser)
                        writeCommunity(community, community.getId()!!)
                    }
                }
            }
        }
    } //UPDATE


    private fun verifyType(data: Data): Int{
        val isCommunity = readCommunity(data.ID)
        val isUser = readUser(data.ID)

        return if(!verifyCommunity(isCommunity) && verifyUser(isUser)) //IS A USER
            0
        else if(verifyCommunity(isCommunity) && !verifyUser(isUser))// IS A COMMUNITY
            1
        else // IS NONE OF THE ABOVE
            -1
    }
}