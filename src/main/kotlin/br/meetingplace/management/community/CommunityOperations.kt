package br.meetingplace.management.community

import br.meetingplace.data.community.CommunityData
import br.meetingplace.data.community.ReportData
import br.meetingplace.data.Data
import br.meetingplace.data.community.ApprovalData
import br.meetingplace.management.dependencies.IDs
import br.meetingplace.management.dependencies.Verify
import br.meetingplace.management.dependencies.fileOperators.rw.*
import br.meetingplace.services.community.Community

class CommunityOperations: ReadWriteUser, ReadWriteLoggedUser, ReadWriteThread, ReadWriteGroup, ReadWriteCommunity, Verify, IDs {
    private fun verifyReportType(data: ReportData): Int{
        val asGroup = readGroup(data.idService)
        val asThread = readThread(data.idService)

        return if(asGroup.getGroupId() == "" && asThread.getId() != "") //IS A THREAD
            0
        else if(asGroup.getGroupId() != "" && asThread.getId() == "")// IS A GROUP
            1
        else // IS NONE OF THE ABOVE
            -1
    }


    fun create(data: CommunityData){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)

        if (verifyLoggedUser(user)){
            val newCommunity = Community.getCommunity()
            val id = data.name.replace("\\s".toRegex(),"").toLowerCase()
            newCommunity.startCommunity(data.name, id, data.about, loggedUser)
            user.social.updateModeratorIn(id,false)
            writeCommunity(newCommunity, id)
            writeUser(user, user.getEmail())
        }
    }

    fun follow(data: Data){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val community = readCommunity(getCommunityId(data.ID))

        if (verifyLoggedUser(user) && verifyCommunity(community) && community.getId() !in user.social.getCommunitiesIFollow() && loggedUser !in community.getFollowers() && loggedUser !in community.getModerators()){
            user.social.updateCommunitiesIFollow(community.getId(), false)
            community.updateFollower(loggedUser, false)
            writeUser(user, loggedUser)
            writeCommunity(community, community.getId())
        }
    }

    fun unfollow(data: Data){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val community = readCommunity(getCommunityId(data.ID))

        if (verifyLoggedUser(user) && verifyCommunity(community) && community.getId() in user.social.getCommunitiesIFollow() && loggedUser in community.getFollowers() && loggedUser !in community.getModerators()){
            user.social.updateCommunitiesIFollow(data.ID, true)
            community.updateFollower(loggedUser, true)
            writeUser(user, loggedUser)
            writeCommunity(community, community.getId())
        }
    }


    fun approveThread(data: ApprovalData){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val community = readCommunity(getCommunityId(data.community))
        val thread = readThread(data.id)

        if(verifyLoggedUser(user) && verifyCommunity(community) && verifyThread(thread) && user.getEmail() in community.getModerators()){
            community.threads.updateThreadsInValidation(data.id,true)
            writeCommunity(community, community.getId())
        }
    }

    fun approveGroup(data: ApprovalData){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val community = readCommunity(getCommunityId(data.community))
        val group = readGroup(data.id)

        if(verifyLoggedUser(user) && verifyCommunity(community) && verifyGroup(group) && user.getEmail() in community.getModerators()){
            community.groups.updateGroupsInValidation(data.id,true)
            writeCommunity(community, community.getId())
        }
    }

    fun report(){

    }

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

}