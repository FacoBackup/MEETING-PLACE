package br.meetingplace.server.controllers.subjects.services.group.factory

import br.meetingplace.server.dto.CreationData

interface GroupFactoryInterface {
    fun create(data: CreationData)
}