package br.meetingplace.server.controllers.subjects.services.search.group

import br.meetingplace.server.dto.SimpleOperator
import br.meetingplace.server.subjects.services.groups.Group

interface GroupSearchInterface {
    fun seeGroup(data: SimpleOperator): Group?
}