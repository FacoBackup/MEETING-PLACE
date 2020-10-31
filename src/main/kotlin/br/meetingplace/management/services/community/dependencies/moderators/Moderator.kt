package br.meetingplace.management.services.community.dependencies.moderators

import br.meetingplace.data.Data
import br.meetingplace.data.community.ApprovalData
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.verify.dependencies.Verify
import br.meetingplace.management.dependencies.readwrite.dependencies.community.ReadWriteCommunity
import br.meetingplace.management.dependencies.readwrite.dependencies.community.ReadWriteReport
import br.meetingplace.management.dependencies.readwrite.dependencies.group.ReadWriteGroup
import br.meetingplace.management.dependencies.readwrite.dependencies.thread.ReadWriteThread
import br.meetingplace.management.dependencies.readwrite.dependencies.user.ReadWriteLoggedUser
import br.meetingplace.management.dependencies.readwrite.dependencies.user.ReadWriteUser

class Moderator private constructor(): ModeratorInterface, ReadWriteUser, ReadWriteLoggedUser, ReadWriteThread, ReadWriteGroup, ReadWriteCommunity, Verify, ReadWriteReport {
    private val iDs = IDsController.getClass()
    companion object{
        private val Class = Moderator()
        fun getClass() = Class
    }

    override fun approveThread(data: ApprovalData){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val community = readCommunity(iDs.getCommunityId(data.community))
        val thread = readThread(data.id)

        if(verifyLoggedUser(user) && verifyCommunity(community) && verifyThread(thread) && user.getEmail() in community.getModerators()){
            //the verify community method already insures that the id and name are different of null so don't mind the !!
            community.updateThreadsInValidation(data.id,true)
            writeCommunity(community, community.getId()!!)
        }
    }

    override fun approveGroup(data: ApprovalData){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val community = readCommunity(iDs.getCommunityId(data.community))
        val group = readGroup(data.id)

        if(verifyLoggedUser(user) && verifyCommunity(community) && verifyGroup(group) && user.getEmail() in community.getModerators()){
            community.updateGroupsInValidation(data.id,true)
            //the verify community method already insures that the id and name are different of null so don't mind the !!
            writeCommunity(community, community.getId()!!)
        }
    }

    override fun stepDown(data: Data){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val community = readCommunity(iDs.getCommunityId(data.ID))

        if(verifyLoggedUser(user) && verifyCommunity(community) && user.getEmail() in community.getModerators() && community.getId() in user.getModeratorIn())
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