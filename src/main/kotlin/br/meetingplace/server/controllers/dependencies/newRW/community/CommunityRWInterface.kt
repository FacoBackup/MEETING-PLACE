package br.meetingplace.server.controllers.dependencies.newRW.community

import br.meetingplace.server.subjects.services.community.Community

interface CommunityRWInterface {
    fun read(id: String): Community
    fun write(data: Community)
    fun delete(data: Community)
}