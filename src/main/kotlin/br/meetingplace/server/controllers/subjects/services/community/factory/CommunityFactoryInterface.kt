package br.meetingplace.server.controllers.subjects.services.community.factory

import br.meetingplace.server.dto.CreationData
import br.meetingplace.server.dto.MemberOperator

interface CommunityFactoryInterface {
    fun create(data: CreationData)
    fun delete(data: MemberOperator)
}