package br.meetingplace.server.controllers.subjects.services.search.community

import br.meetingplace.server.controllers.dependencies.rw.controller.RWController
import br.meetingplace.server.dto.SimpleOperator
import br.meetingplace.server.subjects.services.community.Community

class CommunitySearch private constructor() : CommunitySearchInterface {
    companion object {
        private val Class = CommunitySearch()
        fun getClass() = Class
    }

    private val rw = RWController.getClass()

    override fun searchCommunity(data: SimpleOperator): Community? {
        return data.identifier.owner?.let { rw.readCommunity(it) }
    }
}