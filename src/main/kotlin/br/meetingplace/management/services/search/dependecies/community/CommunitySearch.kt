package br.meetingplace.management.services.search.dependecies.community

import br.meetingplace.data.Data
import br.meetingplace.management.dependencies.IDs
import br.meetingplace.management.dependencies.Verify
import br.meetingplace.management.dependencies.fileOperators.rw.ReadWriteCommunity
import br.meetingplace.services.community.Community

class CommunitySearch private constructor(): CommunitySearchInterface, ReadWriteCommunity, IDs, Verify{

    companion object{
        private val Class = CommunitySearch()
        fun getClass() = Class
    }

    override fun searchCommunity(data: Data):Community?{
        val community = readCommunity(getCommunityId(data.ID))
        return if(verifyCommunity(community))
            community
        else null
    }
}