package br.meetingplace.management.services.community.dependencies.moderators

import br.meetingplace.data.Data
import br.meetingplace.data.community.ApprovalData
import br.meetingplace.management.dependencies.IDs
import br.meetingplace.management.dependencies.Verify
import br.meetingplace.management.dependencies.fileOperators.rw.*

class Moderator private constructor(): ModeratorInterface, ReadWriteUser, ReadWriteLoggedUser, ReadWriteThread, ReadWriteGroup, ReadWriteCommunity, Verify, IDs, ReadWriteReport{

    companion object{
        private val Class = Moderator()
        fun getClass() = Class
    }

    override fun approveThread(data: ApprovalData){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val community = readCommunity(getCommunityId(data.community))
        val thread = readThread(data.id)

        if(verifyLoggedUser(user) && verifyCommunity(community) && verifyThread(thread) && user.getEmail() in community.getModerators()){
            community.updateThreadsInValidation(data.id,true)
            writeCommunity(community, community.getId())
        }
    }

    override fun approveGroup(data: ApprovalData){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val community = readCommunity(getCommunityId(data.community))
        val group = readGroup(data.id)

        if(verifyLoggedUser(user) && verifyCommunity(community) && verifyGroup(group) && user.getEmail() in community.getModerators()){
            community.updateGroupsInValidation(data.id,true)
            writeCommunity(community, community.getId())
        }
    }

    override fun stepDown(data: Data){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val community = readCommunity(getCommunityId(data.ID))

        if(verifyLoggedUser(user) && verifyCommunity(community) && user.getEmail() in community.getModerators())
            community.updateModerator(loggedUser, loggedUser, null)
    }


/*
    fun reportOperation(reportId: String){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val report = readReport(reportId)
        val community = readCommunity(getCommunityId(report.communityId))

        if(verifyLoggedUser(user) && verifyCommunity(community) && user.getEmail() in community.getModerators() && verifyReport(report)){
            when(delete){
                true->{

                }
                false->{

                }
            }
        }
    }

 */
}