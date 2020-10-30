package br.meetingplace.management.services.community.core

import br.meetingplace.data.Data
import br.meetingplace.data.community.ApprovalData
import br.meetingplace.data.community.CommunityData
import br.meetingplace.data.community.ReportData
import br.meetingplace.management.services.community.dependencies.factory.CommunityFactory
import br.meetingplace.management.services.community.dependencies.factory.CommunityFactoryInterface
import br.meetingplace.management.services.community.dependencies.followers.Follower
import br.meetingplace.management.services.community.dependencies.followers.FollowerInterface
import br.meetingplace.management.services.community.dependencies.moderators.Moderator
import br.meetingplace.management.services.community.dependencies.moderators.ModeratorInterface

class CommunityCore private constructor(): FollowerInterface, CommunityFactoryInterface, ModeratorInterface{

    private val factory = CommunityFactory.getClass()
    private val follower = Follower.getClass()
    private val moderator = Moderator.getClass()

    companion object{
        private val Class = CommunityCore()
        fun getClass() = Class
    }

    override fun create(data: CommunityData) {
        factory.create(data)
    }

    override fun delete(data: Data) {
        factory.delete(data)
    }

    override fun createReport(data: ReportData) {
        follower.createReport(data)
    }

    override fun deleteReport(data: ApprovalData) {
        follower.deleteReport(data)
    }

    override fun approveThread(data: ApprovalData){
        moderator.approveThread(data)
    }

    override fun approveGroup(data: ApprovalData){
        moderator.approveGroup(data)
    }

    override fun stepDown(data: Data){
        moderator.stepDown(data)
    }
}