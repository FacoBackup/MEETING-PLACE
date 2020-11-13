package br.meetingplace.server.controllers.subjects.services.search.community

import br.meetingplace.server.controllers.dependencies.readwrite.community.CommunityRWInterface
import br.meetingplace.server.dto.SimpleOperator
import br.meetingplace.server.subjects.services.community.Community

class CommunitySearch private constructor()  {
    companion object {
        private val Class = CommunitySearch()
        fun getClass() = Class
    }

    fun searchCommunity(data: SimpleOperator, rwCommunity: CommunityRWInterface): Community? {
        return rwCommunity.read(data.identifier.ID)
    }
}