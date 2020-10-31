package br.meetingplace.management.services.user.dependencies.follow

import br.meetingplace.data.Data
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.readwrite.controller.ReadWriteController
import br.meetingplace.management.dependencies.verify.controller.VerifyController
import br.meetingplace.services.entitie.User
import br.meetingplace.services.entitie.profiles.followdata.FollowData
import br.meetingplace.services.notification.Inbox

class Follow private constructor(): FollowInterface{
    private val iDs = IDsController.getClass()
    private val rw = ReadWriteController.getClass()
    private val verify = VerifyController.getClass()

    companion object{
        private val Class = Follow()
        fun getClass() = Class
    }

    override fun follow(data: Data){
        val loggedUser = rw.readLoggedUser().email
        val user =rw.readUser(loggedUser)
        lateinit var notification: Inbox
        lateinit var external: User
        lateinit var followingData: FollowData
        lateinit var followerData: FollowData

        if(verify.verifyUser(user)){
            when(verifyType(data)){
                0->{ //USER
                    external = rw.readUser(data.ID)
                    notification = Inbox("${user.getUserName()} is now following you.", "New follower.")
                    if(verify.verifyUser(external) && !verify.verifyFollower(external, user)){

                        //the verify user method already insures that the user name and id are different of null, so don't mind the !!
                        followerData = FollowData( user.getUserName()!!,user.getEmail())
                        followingData = FollowData( external.getUserName()!!,external.getEmail())


                        external.updateInbox(notification)
                        external.updateFollowers(followerData,false)
                        user.updateFollowing(followingData,false)

                        rw.writeUserToFile(user,iDs.attachNameToEmail(user.getUserName(),user.getEmail()))
                        rw.writeUserToFile(external,iDs.attachNameToEmail(external.getUserName(),external.getEmail()))
                    }
                }
                1->{ //COMMUNITY
                    val community = rw.readCommunity(iDs.getCommunityId(data.ID))

                    if (verify.verifyCommunity(community)  && loggedUser !in community.getFollowers() && loggedUser !in community.getModerators() && community.getId() !in user.getCommunitiesIFollow() && community.getId() !in user.getModeratorIn()){
                        //the verify community method already insures that the id and name are different of null so don't mind the !!
                        user.updateCommunitiesIFollow(community.getId()!!, false)
                        community.updateFollower(loggedUser, false)

                        rw.writeUserToFile(user,iDs.attachNameToEmail(user.getUserName(),user.getEmail()))
                        rw.writeCommunity(community, community.getId()!!)
                    }
                }
            }

        }
    } //UPDATE

    override fun unfollow(data: Data){
        val loggedUser = rw.readLoggedUser().email
        val user = rw.readUser(loggedUser)

        lateinit var followingData: FollowData
        lateinit var followerData: FollowData
        lateinit var external: User
        if(verify.verifyUser(user)){
            when(verifyType(data)){
                0->{ //USER
                    external = rw.readUser(data.ID)
                    if(external.getAge() != -1 && verify.verifyUser(external) && verify.verifyFollower(external, user)){

                        //the verify user method already insures that the user name and id are different of null, so don't mind the !!
                        followerData = FollowData( user.getUserName(),user.getEmail())
                        followingData = FollowData( external.getUserName(),external.getEmail())

                        external.updateFollowers(followerData,true)
                        user.updateFollowing(followingData,true)
                        rw.writeUserToFile(user,iDs.attachNameToEmail(user.getUserName(),user.getEmail()))
                        rw.writeUserToFile(external,iDs.attachNameToEmail(external.getUserName(),external.getEmail()))
                    }
                }
                1->{ //COMMUNITY
                    val community = rw.readCommunity(iDs.getCommunityId(data.ID))
                    if (verify.verifyCommunity(community)){

                        when(community.getId() in user.getCommunitiesIFollow() && loggedUser in community.getFollowers()){
                            true->{
                                user.updateCommunitiesIFollow(data.ID, true)
                                community.updateFollower(loggedUser, true)
                            }
                        }

                        //the verify community method already insures that the id and name are different of null so don't mind the !!
                        rw.writeUserToFile(user,iDs.attachNameToEmail(user.getUserName(),user.getEmail()))
                        rw.writeCommunity(community, community.getId()!!)
                    }
                }
            }
        }
    } //UPDATE


    private fun verifyType(data: Data): Int{
        val isCommunity = rw.readCommunity(data.ID)
        val isUser = rw.readUser(data.ID)

        return if(!verify.verifyCommunity(isCommunity) && verify.verifyUser(isUser)) //IS A USER
            0
        else if(verify.verifyCommunity(isCommunity) && !verify.verifyUser(isUser))// IS A COMMUNITY
            1
        else // IS NONE OF THE ABOVE
            -1
    }
}