package br.meetingplace.server.controllers.subjects.services.group.delete

import br.meetingplace.server.dto.SimpleOperator

interface GroupDeleteInterface {
    fun delete(data: SimpleOperator)
}