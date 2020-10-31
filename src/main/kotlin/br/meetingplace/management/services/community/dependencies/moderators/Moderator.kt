package br.meetingplace.management.services.community.dependencies.moderators

import br.meetingplace.data.Data
import br.meetingplace.data.community.ApprovalData
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.readwrite.controller.ReadWriteController
import br.meetingplace.management.dependencies.verify.controller.VerifyController

class Moderator private constructor(): ModeratorInterface{
    private val rw = ReadWriteController.getClass()
    private val verify = VerifyController.getClass()
    private val iDs = IDsController.getClass()
    companion object{
        private val Class = Moderator()
        fun getClass() = Class
    }

    override fun approveThread(data: ApprovalData){
        val logged = rw.readLoggedUser().fileName
        val user = rw.readUser(if(!logged.isNullOrBlank()) logged else "")

        val community =  rw.readCommunity(iDs.getCommunityId(data.community))
        val thread =  rw.readThread(data.id)

        if(verify.verifyUser(user) && verify.verifyCommunity(community) && verify.verifyThread(thread) && user.getEmail() in community.getModerators()){
            //the verify community method already insures that the id and name are different of null so don't mind the !!
            community.updateThreadsInValidation(data.id,true)
            rw.writeCommunity(community, community.getId()!!)
        }
    }

    override fun approveGroup(data: ApprovalData){
        val logged = rw.readLoggedUser().fileName
        val user = rw.readUser(if(!logged.isNullOrBlank()) logged else "")

        val community =  rw.readCommunity(iDs.getCommunityId(data.community))
        val group =  rw.readGroup(data.id)

        if(verify.verifyUser(user) && verify.verifyCommunity(community) && verify.verifyGroup(group) && user.getEmail() in community.getModerators()){
            community.updateGroupsInValidation(data.id,true)
            //the verify community method already insures that the id and name are different of null so don't mind the !!
            rw.writeCommunity(community, community.getId()!!)
        }
    }

    override fun stepDown(data: Data){
        val logged = rw.readLoggedUser().fileName
        val user = rw.readUser(if(!logged.isNullOrBlank()) logged else "")

        val community =  rw.readCommunity(iDs.getCommunityId(data.ID))

        if(verify.verifyUser(user) && verify.verifyCommunity(community) && user.getEmail() in community.getModerators() && community.getId() in user.getModeratorIn())
            community.updateModerator(logged, logged, null)
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