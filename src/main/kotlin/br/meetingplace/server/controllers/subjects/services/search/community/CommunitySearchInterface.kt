package br.meetingplace.server.controllers.subjects.services.search.community

import br.meetingplace.server.dto.SimpleOperator
import br.meetingplace.server.subjects.services.community.Community

interface CommunitySearchInterface {
    fun searchCommunity(data: SimpleOperator): Community?
}