package br.meetingplace.management.services.search.dependecies.community

import br.meetingplace.data.Data
import br.meetingplace.services.community.Community

class CommunitySearch private constructor(): CommunitySearchInterface{

    companion object{
        private val Class = CommunitySearch()
        fun getClass() = Class
    }

    override fun searchCommunity(data: Data): List<Community> {
        TODO("Not yet implemented")
    }
}