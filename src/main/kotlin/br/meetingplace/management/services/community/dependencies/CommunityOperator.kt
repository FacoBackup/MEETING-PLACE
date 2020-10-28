package br.meetingplace.management.services.community.dependencies

import br.meetingplace.data.community.CommunityData
import br.meetingplace.data.community.ReportData
import br.meetingplace.management.dependencies.Generator
import br.meetingplace.management.dependencies.IDs
import br.meetingplace.management.dependencies.Verify
import br.meetingplace.management.dependencies.fileOperators.rw.*
import br.meetingplace.services.community.Community
import br.meetingplace.services.community.data.Report

class CommunityOperator: Moderators(), ReadWriteUser, ReadWriteLoggedUser, ReadWriteCommunity, Verify, IDs, Generator{

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

    fun createReport(data: ReportData){
        val loggedUser = readLoggedUser().email
        val user = readUser(loggedUser)
        val community = readCommunity(getCommunityId(data.communityId))
        val thread = readThread(data.idService)

        if (verifyLoggedUser(user) && verifyCommunity(community) && verifyThread(thread)){
            val newReport = Report(generateId(), loggedUser, data.idService, data.reason, false, community.getId(), null)
            community.threads.updateReport(newReport, false)
            writeCommunity(community, community.getId())
            writeReport(newReport, newReport.reportId)
        }
    }
}