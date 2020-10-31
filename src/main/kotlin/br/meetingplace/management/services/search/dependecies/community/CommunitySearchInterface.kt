package br.meetingplace.management.services.search.dependecies.community

import br.meetingplace.data.Data
import br.meetingplace.services.community.Community

interface CommunitySearchInterface {
    fun searchCommunity(data: Data): Community?
}