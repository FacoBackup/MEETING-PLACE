package br.meetingplace.management.services.search.dependecies.community

import br.meetingplace.data.Data
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.verify.dependencies.Verify
import br.meetingplace.management.dependencies.readwrite.dependencies.community.ReadWriteCommunity
import br.meetingplace.services.community.Community

class CommunitySearch private constructor(): CommunitySearchInterface, ReadWriteCommunity, Verify {
    private val iDs = IDsController.getClass()
    companion object{
        private val Class = CommunitySearch()
        fun getClass() = Class
    }

    override fun searchCommunity(data: Data):Community?{
        val community = readCommunity(iDs.getCommunityId(data.ID))
        return if(verifyCommunity(community))
            community
        else null
    }
}