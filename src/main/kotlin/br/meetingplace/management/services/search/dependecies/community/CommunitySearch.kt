package br.meetingplace.management.services.search.dependecies.community

import br.meetingplace.data.Data
import br.meetingplace.management.dependencies.idmanager.controller.IDsController
import br.meetingplace.management.dependencies.readwrite.controller.ReadWriteController
import br.meetingplace.management.dependencies.verify.controller.VerifyController
import br.meetingplace.services.community.Community

class CommunitySearch private constructor(): CommunitySearchInterface{
    private val iDs = IDsController.getClass()
    private val rw = ReadWriteController.getClass()
    private val verify = VerifyController.getClass()

    companion object{
        private val Class = CommunitySearch()
        fun getClass() = Class
    }

    override fun searchCommunity(data: Data):Community?{
        val community = rw.readCommunity(iDs.getCommunityId(data.ID))
        return if(verify.verifyCommunity(community))
            community
        else null
    }
}